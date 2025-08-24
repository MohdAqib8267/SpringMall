package com.ecomm.product_service.dtos;

import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record ProductPurchaseResponse(
        Integer productLineId,
        Integer productId,
        String name,
        String description,
        BigDecimal price,
        double quantity
) {
}
