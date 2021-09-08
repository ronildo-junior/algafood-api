package com.ronijr.algafoodapi.config.mapper;

import com.ronijr.algafoodapi.api.model.OrderItemModel;
import com.ronijr.algafoodapi.api.model.OrderModel;
import com.ronijr.algafoodapi.domain.model.Order;
import com.ronijr.algafoodapi.domain.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order inputToEntity(OrderModel.Input input);
    OrderModel.Output entityToOutput(Order entity);
    OrderModel.Summary entityToSummary(Order entity);

    @Mapping(target = "product.id", source = "productId")
    OrderItem inputToEntity(OrderItemModel.Input input);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    OrderItemModel.Output entityToOutput(OrderItem entity);
}