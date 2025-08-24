package com.example.Order.Product;

import java.math.BigDecimal;

public record PurchaseResponse(

        int id,
        String name,
        String description,
        BigDecimal price,
        double quantity
) {
}
