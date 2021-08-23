package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.Cuisine;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuisineRepository extends CustomJpaRepository<Cuisine, Long> {
    List<Cuisine> findAllByNameContaining(String name);
}
