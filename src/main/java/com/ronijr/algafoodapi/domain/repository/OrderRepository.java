package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CustomJpaRepository<Order, Long> {
    @Query("from Order o join fetch o.customer join fetch o.restaurant r join fetch r.cuisine " +
            "join fetch r.address.city c left join fetch c.state order by o.id")
    List<Order> findAll();
}