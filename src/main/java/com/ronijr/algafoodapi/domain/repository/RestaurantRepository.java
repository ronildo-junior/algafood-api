package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.Restaurant;

import java.util.List;

public interface RestaurantRepository {
    List<Restaurant> list();
    Restaurant get(Long id);
    Restaurant add(Restaurant restaurant);
    void remove(Long id);
}
