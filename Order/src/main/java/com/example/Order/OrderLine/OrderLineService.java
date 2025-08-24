package com.example.Order.OrderLine;

import com.example.Order.Modal.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderLineService {

    private final OrderLineRepository orderlineRepository;

    @Autowired
    private OrderLineMapper mapper;

    @Transactional // participates in same transaction when called from OrderService.createOrder
    public int saveOrderLine(OrderLineRequest request) {
        var order = mapper.toOrderLine(request);
        return orderlineRepository.save(order).getId();
    }
    public void compensateOrderLines(UUID orderId) {
        orderlineRepository.deleteAllByOrder_OrderId(orderId);
    }

    public List<OrderLineResponse> findAllByOrderId(UUID orderId) {
        return orderlineRepository.findAllByOrder_OrderId(orderId).stream()
                .map(mapper::toOrderLineResponse)
                .collect(Collectors.toList());

    }


}
