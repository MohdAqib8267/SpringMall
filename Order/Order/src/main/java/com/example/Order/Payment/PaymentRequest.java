package com.example.Order.Payment;

import com.example.Order.Customer.CustomerResponse;
import com.example.Order.Modal.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
