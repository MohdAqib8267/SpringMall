package com.ecomm.customer.Modal;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@Setter
@Getter
@Builder
//@RequiredArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String role = "USER";
    private Boolean communicationSw;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> tokens;
}
