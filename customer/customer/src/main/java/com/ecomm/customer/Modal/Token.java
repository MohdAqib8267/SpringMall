package com.ecomm.customer.Modal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String accessToken;
    private String refreshToken;

    private boolean expired;
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
