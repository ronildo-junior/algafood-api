package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuisineRepository extends JpaRepository<Cuisine, Long> {
    List<Cuisine> findByNameContaining(String name);
}
