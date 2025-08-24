package com.example.Order.OrderLine;

public record OrderLineResponse(
        int id,
        int productId,
        double quantity
) {
}
