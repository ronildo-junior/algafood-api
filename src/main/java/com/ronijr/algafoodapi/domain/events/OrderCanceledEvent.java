package com.ronijr.algafoodapi.domain.events;

import com.ronijr.algafoodapi.domain.model.Order;
import lombok.Value;

@Value
public class OrderCanceledEvent {
    Order order;
}