package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.OrderModel;
import com.ronijr.algafoodapi.config.mapper.OrderMapper;
import com.ronijr.algafoodapi.domain.model.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OrderAssembler {
    private final OrderMapper mapper;

    public OrderModel.Output toOutput(Order order) {
        return mapper.entityToOutput(order);
    }

    public OrderModel.Summary toSummary(Order order) {
        return mapper.entityToSummary(order);
    }

    public OrderModel.StatusInfo toStatusInfo(Order order) {
        return mapper.entityToStatusInfo(order);
    }

    public List<OrderModel.Summary> toCollectionModel(Collection<Order> orders) {
        return orders.stream().
                map(this::toSummary).
                collect(Collectors.toList());
    }
}