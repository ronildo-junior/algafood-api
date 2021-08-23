package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RestaurantRepository
        extends CustomJpaRepository<Restaurant, Long>, RestaurantRepositoryQueries {
    @Override
    List<Restaurant> findCriteriaTest(Map<String, Object> parameters);
}
