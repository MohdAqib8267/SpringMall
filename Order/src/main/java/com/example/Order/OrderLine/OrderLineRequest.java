package com.example.Order.OrderLine;

import java.util.UUID;

public record OrderLineRequest(
        Integer id,
        UUID orderId,
        int productId,
        double quantity
) {
}
