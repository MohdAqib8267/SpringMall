package com.ecomm.notification.Kafka.Order;

public record Customer(
        Integer id,
        String firstname,
        String lastname,
        String email
) {
}
