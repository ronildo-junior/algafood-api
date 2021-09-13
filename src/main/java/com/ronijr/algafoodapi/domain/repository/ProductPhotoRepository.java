package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.ProductPhoto;

import java.util.Optional;

public interface ProductPhotoRepository {
    ProductPhoto save(ProductPhoto productPhoto);
    Optional<ProductPhoto> findPhotoByIdAndRestaurantId(Long productId, Long restaurantId);
    void delete(ProductPhoto productPhoto);
}