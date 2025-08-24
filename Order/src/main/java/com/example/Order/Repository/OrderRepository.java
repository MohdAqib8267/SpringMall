package com.example.Order.Repository;

import com.example.Order.Modal.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order,UUID> {

    void deleteByOrderId(UUID orderId);
}
