package com.beautyshop.backend.controller;

import com.beautyshop.backend.model.Order;
import com.beautyshop.backend.model.OrderStatus;
import com.beautyshop.backend.model.Transaction;
import com.beautyshop.backend.repository.OrderRepository;
import com.beautyshop.backend.repository.TransactionRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
public class WebhookController {

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    private final OrderRepository orderRepository;
    private final TransactionRepository transactionRepository;

    @PostMapping
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer()
                    .getObject()
                    .orElse(null);

            if (session != null) {
                String orderIdStr = session.getMetadata().get("orderId");
                Long orderId = Long.parseLong(orderIdStr);

                Order order = orderRepository.findById(orderId).orElse(null);

                if (order != null) {
                    order.setStatus(OrderStatus.PAID);
                    order.setStripePaymentId(session.getId());
                    orderRepository.save(order);

                    order.getItems().forEach(item -> {
                        item.getProduct().setQuantity(
                                item.getProduct().getQuantity() - item.getQuantity()
                        );
                    });

                    Transaction transaction = new Transaction();
                    transaction.setOrder(order);
                    transaction.setStripeEventId(event.getId());
                    transaction.setAmount(BigDecimal.valueOf(session.getAmountTotal())
                            .divide(BigDecimal.valueOf(100)));
                    transaction.setCurrency(session.getCurrency());
                    transaction.setStatus("COMPLETED");
                    transactionRepository.save(transaction);
                }
            }
        }

        return ResponseEntity.ok("Success");
    }
}
