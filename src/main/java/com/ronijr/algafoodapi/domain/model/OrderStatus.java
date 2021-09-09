package com.ronijr.algafoodapi.domain.model;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    CREATED("created"),
    CONFIRMED("confirmed", CREATED),
    DELIVERED("delivered", CONFIRMED),
    CANCELLED("cancelled", CREATED);

    private final String value;
    private final List<OrderStatus> previousStatus;

    OrderStatus(String value, OrderStatus... previousStatus) {
        this.value = value;
        this.previousStatus = Arrays.asList(previousStatus);
    }

    public String getValue() {
        return this.value;
    }

    public boolean allowTransitionTo(OrderStatus status) {
        return status.previousStatus.contains(this);
    }
}