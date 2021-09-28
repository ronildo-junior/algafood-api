package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.OrderModel;
import com.ronijr.algafoodapi.config.mapper.OrderMapper;
import com.ronijr.algafoodapi.domain.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.api.hateoas.AlgaLinks.*;

@Component
public class OrderSummaryAssembler extends RepresentationModelAssemblerSupport<Order, OrderModel.Summary> {
    @Autowired
    private OrderMapper mapper;

    public OrderSummaryAssembler() {
        super(Order.class, OrderModel.Summary.class);
    }

    @Override
    public OrderModel.Summary toModel(Order order) {
        OrderModel.Summary model = mapper.entityToSummary(order);

        model.add(linkToOrder(order.getCode()));
        model.add(linkToStatusOrder(order.getCode(), "status-info"));
        model.add(linkToOrders("order-list"));

        model.getCustomer().add(linkToUser(order.getCustomer().getId()));
        model.getRestaurant().add(linkToRestaurant(order.getRestaurant().getId()));
        return model;
    }

    @Override
    public CollectionModel<OrderModel.Summary> toCollectionModel(Iterable<? extends Order> orders) {
        return super.toCollectionModel(orders).add(linkToOrders("order-list"));
    }
}