package com.beautyshop.backend.controller;

import com.beautyshop.backend.dto.OrderDTO;
import com.beautyshop.backend.model.User;
import com.beautyshop.backend.service.OrderService;
import com.beautyshop.backend.service.StripeService;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class StripeController {

    private final StripeService stripeService;
    private final OrderService orderService;

    @PostMapping("/create-checkout-session/{orderId}")
    public ResponseEntity<Map<String, String>> createCheckoutSession(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId) {
        try {
            OrderDTO order = orderService.getOrderById(orderId);

            long amount = order.getTotalPrice()
                    .multiply(BigDecimal.valueOf(100))
                    .longValue();

            Session session = stripeService.createCheckoutSession(orderId, amount, "eur");

            // Sacuvaj payment intent ID na orderu
            if (session.getPaymentIntent() != null) {
                orderService.updateStripePaymentId(orderId, session.getPaymentIntent());
            }

            Map<String, String> response = new HashMap<>();
            response.put("sessionId", session.getId());
            response.put("url", session.getUrl());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}