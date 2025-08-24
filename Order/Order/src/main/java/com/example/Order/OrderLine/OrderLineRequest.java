package com.example.Order.OrderLine;

public record OrderLineRequest(
        Integer id,
        int orderId,
        int productId,
        double quantity
) {
}
