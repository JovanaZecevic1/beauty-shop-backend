package com.beautyshop.backend.controller;

import com.beautyshop.backend.model.Order;
import com.beautyshop.backend.model.OrderStatus;
import com.beautyshop.backend.model.Transaction;
import com.beautyshop.backend.repository.OrderRepository;
import com.beautyshop.backend.repository.TransactionRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    @PostMapping("/stripe")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        System.out.println("Webhook event type: " + event.getType());

        if ("checkout.session.completed".equals(event.getType())) {
            try {
                JsonObject session = JsonParser.parseString(payload)
                        .getAsJsonObject()
                        .getAsJsonObject("data")
                        .getAsJsonObject("object");

                JsonObject metadata = session.getAsJsonObject("metadata");
                String orderIdStr = metadata.get("orderId").getAsString();
                System.out.println("Order ID: " + orderIdStr);

                Long orderId = Long.parseLong(orderIdStr);
                Order order = orderRepository.findById(orderId).orElse(null);

                if (order != null) {
                    order.setStatus(OrderStatus.PAID);
                    order.setStripePaymentId(session.get("id").getAsString());
                    orderRepository.save(order);
                    System.out.println("Order " + orderId + " PAID!");

                    Transaction transaction = new Transaction();
                    transaction.setOrder(order);
                    transaction.setStripeEventId(event.getId());
                    transaction.setAmount(BigDecimal.valueOf(
                                    session.get("amount_total").getAsLong())
                            .divide(BigDecimal.valueOf(100)));
                    transaction.setCurrency(session.get("currency").getAsString());
                    transaction.setStatus("COMPLETED");
                    transactionRepository.save(transaction);
                } else {
                    System.out.println("Order not found: " + orderId);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return ResponseEntity.ok("Success");
    }
}