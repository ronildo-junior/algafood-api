package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.OrderModel;
import com.ronijr.algafoodapi.config.mapper.OrderMapper;
import com.ronijr.algafoodapi.domain.model.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderDisassembler {
    private final OrderMapper mapper;

    public Order toDomain(OrderModel.Input input) {
        return mapper.inputToEntity(input);
    }
}