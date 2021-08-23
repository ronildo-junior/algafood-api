package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.Restaurant;

import java.util.List;
import java.util.Map;

public interface RestaurantRepositoryQueries {
    List<Restaurant> findCriteriaTest(Map<String, Object> parameters);
}
