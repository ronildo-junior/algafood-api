package com.ronijr.algafoodapi.api.v1.assembler;

import com.ronijr.algafoodapi.api.v1.model.OrderModel;
import com.ronijr.algafoodapi.config.mapper.OrderMapper;
import com.ronijr.algafoodapi.config.security.AlgaSecurity;
import com.ronijr.algafoodapi.domain.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.*;

@Component
public class OrderSummaryAssembler extends RepresentationModelAssemblerSupport<Order, OrderModel.Summary> {
    @Autowired
    private OrderMapper mapper;
    @Autowired
    private AlgaSecurity algaSecurity;

    public OrderSummaryAssembler() {
        super(Order.class, OrderModel.Summary.class);
    }

    @Override
    public OrderModel.Summary toModel(Order order) {
        OrderModel.Summary model = mapper.entityToSummary(order);
        model.add(linkToOrder(order.getCode()));
        model.add(linkToStatusOrder(order.getCode(), "status-info"));
        if (algaSecurity.allowQueryOrders()) {
            model.add(linkToOrders("order-list"));
        }
        if (algaSecurity.allowQueryUsers()) {
            model.getCustomer().add(linkToUser(order.getCustomer().getId()));
        }
        if (algaSecurity.allowQueryRestaurants()) {
            model.getRestaurant().add(linkToRestaurant(order.getRestaurant().getId()));
        }
        return model;
    }

    @Override
    public CollectionModel<OrderModel.Summary> toCollectionModel(Iterable<? extends Order> orders) {
        return super.toCollectionModel(orders).add(linkToOrders("order-list"));
    }
}