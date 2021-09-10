package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.Product;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface ProductRepository extends CustomJpaRepository<Product, Long> {
    Optional<Product> findByIdAndRestaurantId(Long id, Long restaurantId);
    @Query("from Product p where p.active = true and p.restaurant.id = :restaurantId")
    Set<Product> findAllActiveByRestaurantId(Long restaurantId);
}