package com.example.Order.Modal;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Builder
public record OrderDetailsItem(
        Integer id,
        double quantity,
        String name,
        String description,
        BigDecimal price,
        Integer categoryId,
        String categoryName,
        String categoryDescription
) {
}
