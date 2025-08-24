package com.example.Order.OrderLine;

import com.example.Order.Modal.OrderResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine,Integer> {
    List<OrderLine> findAllByOrder_OrderId(UUID orderId);

    void deleteAllByOrder_OrderId(UUID orderId);
}
