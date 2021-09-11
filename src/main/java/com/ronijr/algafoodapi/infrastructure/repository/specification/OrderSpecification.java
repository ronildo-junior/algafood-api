package com.ronijr.algafoodapi.infrastructure.repository.specification;

import com.ronijr.algafoodapi.domain.filter.OrderFilter;
import com.ronijr.algafoodapi.domain.model.Order;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;

public final class OrderSpecification {
    private OrderSpecification(){}
    public static Specification<Order> doFilter(OrderFilter orderFilter) {
        return (root, query, builder) -> {
            if( Order.class.equals(query.getResultType())) {
                root.fetch("restaurant").
                        fetch("cuisine");
                root.fetch("customer");
                root.fetch("deliveryAddress").
                        fetch("city").
                        fetch("state");
            }
            var predicates = new ArrayList<Predicate>();
            if (orderFilter.getCustomerId() != null) {
                predicates.add(builder.equal(root.get("customer"), orderFilter.getCustomerId()));
            }
            if (orderFilter.getRestaurantId() != null) {
                predicates.add(builder.equal(root.get("restaurant"), orderFilter.getRestaurantId()));
            }
            if (orderFilter.getCreationDateFirst() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), orderFilter.getCreationDateFirst()));
            }
            if (orderFilter.getCreationDateLast() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("createdAt"), orderFilter.getCreationDateLast()));
            }
            return  builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}