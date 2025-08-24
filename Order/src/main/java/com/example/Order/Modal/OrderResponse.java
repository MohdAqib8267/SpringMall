package com.example.Order.Modal;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderResponse(
    UUID orderId,
    String reference,
    BigDecimal amount,
    PaymentMethod paymentMethod,
    int customerId
) {
}
