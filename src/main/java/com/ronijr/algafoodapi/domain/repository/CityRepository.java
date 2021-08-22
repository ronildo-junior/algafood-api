package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.City;

import java.util.List;

public interface CityRepository {
    List<City> list();
    City get(Long id);
    City add(City city);
    void remove(Long id);
}
