package com.example.Order.OrderLine;

import com.example.Order.Modal.Order;
import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
@NoArgsConstructor
@Table(name = "customer_line")
public class OrderLine {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private int productId;
    private double quantity;

}
