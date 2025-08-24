package com.example.Order.OrderLine;

import com.example.Order.Modal.Order;
import com.example.Order.Modal.OrderResponse;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
public class OrderLineMapper {

    public OrderLine toOrderLine(OrderLineRequest request){
        return OrderLine.builder()
                .id(request.id())
                .quantity(request.quantity())
                .order(
                        Order.builder()
                                .id(request.orderId())
                                .build()
                )
                .productId(request.productId())
                .build();
//        Order order = new Order();
//        order.setId(request.id());
//        OrderLine newOrderLine = new OrderLine(request.id(), request.quantity(),order, request.productId());
//        return newOrderLine;

    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(
            orderLine.getId(),
                orderLine.getQuantity()
        );
    }
}
