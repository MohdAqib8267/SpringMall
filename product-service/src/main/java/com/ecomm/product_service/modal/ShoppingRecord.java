package com.ecomm.product_service.modal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingRecord {
    @Id
    @GeneratedValue
    private Integer orderLineId;
    @Column(name = "order_id",columnDefinition = "uuid")
    private UUID orderId;
    private Integer userId;
    private Integer productId;
    private LocalDateTime dateOfPurchase;
}
