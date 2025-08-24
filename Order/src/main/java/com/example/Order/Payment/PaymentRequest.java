package com.example.Order.Payment;

import com.example.Order.Customer.CustomerResponse;
import com.example.Order.Modal.PaymentMethod;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        UUID orderId,
        String orderReference,
        CustomerResponse customer
) {
}
