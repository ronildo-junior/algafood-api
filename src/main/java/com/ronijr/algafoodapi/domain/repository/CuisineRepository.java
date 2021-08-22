package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.Cuisine;

import java.util.List;

public interface CuisineRepository {
    List<Cuisine> list();
    List<Cuisine> filterByName(String name);
    Cuisine get(Long id);
    Cuisine add(Cuisine cuisine);
    void remove(Long id);
}
