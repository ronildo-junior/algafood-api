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
public class OrderAssembler extends RepresentationModelAssemblerSupport<Order, OrderModel.Output> {
    @Autowired
    private OrderMapper mapper;
    @Autowired
    private AlgaSecurity algaSecurity;

    public OrderAssembler() {
        super(Order.class, OrderModel.Output.class);
    }

    @Override
    public OrderModel.Output toModel(Order order) {
        OrderModel.Output model = mapper.entityToOutput(order);

        model.add(linkToOrder(order.getCode()));
        if (algaSecurity.allowManageOrder(order.getCode())) {
            if (order.canCancel()) {
                model.add(linkToOrderCancellation(order.getCode(), "cancellation"));
            }
            if (order.canConfirm()) {
                model.add(linkToOrderConfirmation(order.getCode(), "confirmation"));
            }
            if (order.canDelivery()) {
                model.add(linkToOrderDelivery(order.getCode(), "delivery"));
            }
        }
        model.add(linkToStatusOrder(order.getCode(), "status-info"));
        model.add(linkToOrders("order-list"));

        model.getCustomer().add(linkToUser(order.getCustomer().getId()));
        model.getDeliveryAddress().getCity().add(linkToCity(order.getDeliveryAddress().getCity().getId()));
        model.getPaymentMethod().add(linkToPaymentMethod(order.getPaymentMethod().getId()));
        model.getRestaurant().add(linkToRestaurant(order.getRestaurant().getId()));
        model.getItems().forEach(item ->
            item.add(linkToProduct(order.getRestaurant().getId(), item.getProductId(), "product"))
        );

        return model;
    }

    @Override
    public CollectionModel<OrderModel.Output> toCollectionModel(Iterable<? extends Order> orders) {
        return super.toCollectionModel(orders).add(linkToOrders("order-list"));
    }
}