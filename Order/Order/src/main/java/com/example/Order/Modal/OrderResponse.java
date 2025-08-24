package com.example.Order.Modal;

import java.math.BigDecimal;

public record OrderResponse(
    int id,
    String reference,
    BigDecimal amount,
    PaymentMethod paymentMethod,
    int customerId
) {
}
