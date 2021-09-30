package com.ronijr.algafoodapi.api.v1.assembler;

import com.ronijr.algafoodapi.api.v1.model.OrderModel;
import com.ronijr.algafoodapi.config.mapper.OrderMapper;
import com.ronijr.algafoodapi.domain.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.linkToOrder;
import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.linkToStatusOrder;

@Component
public class OrderStatusAssembler extends RepresentationModelAssemblerSupport<Order, OrderModel.StatusInfo> {
    @Autowired
    private OrderMapper mapper;

    public OrderStatusAssembler() {
        super(Order.class, OrderModel.StatusInfo.class);
    }

    @Override
    public OrderModel.StatusInfo toModel(Order order) {
        OrderModel.StatusInfo model = mapper.entityToStatusInfo(order);
        model.add(linkToStatusOrder(order.getCode()));
        model.add(linkToOrder(order.getCode(), "order"));
        return model;
    }
}