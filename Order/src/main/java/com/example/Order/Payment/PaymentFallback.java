package com.example.Order.Payment;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentFallback implements PaymentClient {
    @Override
    public UUID requestOrderPayment(PaymentRequest request) {
        return null;
    }
}
