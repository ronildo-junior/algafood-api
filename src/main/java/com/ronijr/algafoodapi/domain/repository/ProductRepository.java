package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.Product;

import java.util.Optional;

public interface ProductRepository extends CustomJpaRepository<Product, Long> {
    Optional<Product> findByIdAndRestaurantId(Long id, Long restaurantId);
}