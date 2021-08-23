package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.City;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends CustomJpaRepository<City, Long> {

}
