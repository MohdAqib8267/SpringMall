package com.example.Order.OrderLine;

import com.example.Order.Modal.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderLineService {

    private final OrderLineRepository orderlineRepository;

    @Autowired
    private OrderLineMapper mapper;


    public int saveOrderLine(OrderLineRequest request) {
        var order = mapper.toOrderLine(request);
        return orderlineRepository.save(order).getId();
    }

    public List<OrderLineResponse> findAllByOrderId(int id) {
        return orderlineRepository.findAllByOrderId(id).stream()
                .map(mapper::toOrderLineResponse)
                .collect(Collectors.toList());

    }
}
