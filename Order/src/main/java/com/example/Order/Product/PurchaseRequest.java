
package com.example.Order.Product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PurchaseRequest(
        @NotNull(message = "product is mandatory")
        int productId,
        @Positive(message = "quantity is mandatory")
        double quantity
) {
}