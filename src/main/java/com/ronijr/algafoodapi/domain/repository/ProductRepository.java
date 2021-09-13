package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.Product;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface ProductRepository extends CustomJpaRepository<Product, Long>, ProductPhotoRepository {
    Optional<Product> findByIdAndRestaurantId(Long productId, Long restaurantId);
    @Query("from Product p where p.active = true and p.restaurant.id = :restaurantId")
    Set<Product> findAllActiveByRestaurantId(Long restaurantId);
    @Query("select count(p) > 0 from Product p where p.id = :productId and p.restaurant.id = :restaurantId")
    boolean existsProductWithIdAndRestaurantId(Long productId, Long restaurantId);
}