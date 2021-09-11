package com.ronijr.algafoodapi.infrastructure.service;

import com.ronijr.algafoodapi.domain.filter.DailySaleFilter;
import com.ronijr.algafoodapi.domain.model.Order;
import com.ronijr.algafoodapi.domain.model.OrderStatus;
import com.ronijr.algafoodapi.domain.model.dto.DailySale;
import com.ronijr.algafoodapi.domain.service.SaleQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class SaleQueryImpl implements SaleQuery {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<DailySale> queryDailySales(DailySaleFilter filter) {
        final String CREATION_DATE = "createdAt";

        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(DailySale.class);
        var root = query.from(Order.class);

        var functionDate = builder.function("date", LocalDate.class, root.get(CREATION_DATE));

        var select = builder.construct(
                DailySale.class,
                functionDate,
                builder.count(root.get("id")),
                builder.sum(root.get("total")));

        var predicates = new ArrayList<Predicate>();

        predicates.add(root.get("status").in(OrderStatus.CONFIRMED, OrderStatus.DELIVERED));

        if (filter.getRestaurantId() != null) {
            predicates.add(builder.equal(root.get("restaurant"), filter.getRestaurantId()));
        }
        if (filter.getCreationDateFirst() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get(CREATION_DATE), filter.getCreationDateFirst()));
        }
        if (filter.getCreationDateLast() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get(CREATION_DATE), filter.getCreationDateLast()));
        }

        query.select(select);
        query.where(predicates.toArray(Predicate[]::new));
        query.groupBy(functionDate);

        return entityManager.createQuery(query).getResultList();
    }
}