package com.beautyshop.backend.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String secretKey;

    public Session createCheckoutSession(Long orderId, Long amount, String currency) throws StripeException {
        Stripe.apiKey = secretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/payment-success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:4200/payment-cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(currency)
                                                .setUnitAmount(amount)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Beauty Shop Order #" + orderId)
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .putMetadata("orderId", orderId.toString())
                .build();

        return Session.create(params);
    }
}
