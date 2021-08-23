package com.ronijr.algafoodapi.infrastructure.repository.specification;

import com.ronijr.algafoodapi.domain.model.Restaurant;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public final class RestaurantSpecification {
    private RestaurantSpecification(){}
    public static Specification<Restaurant> withFreeDelivery(){
        return (root, query, builder) -> builder.equal(root.get("deliveryFee"), BigDecimal.ZERO);
    }
}
