package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.Product;
import com.ronijr.algafoodapi.domain.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductQuery {
    private final ProductRepository repository;
    private final AppMessageSource messageSource;

    public Optional<Product> findById(Long productId, Long restaurantId) {
        return repository.findByIdAndRestaurantId(productId, restaurantId);
    }

    public Product findByIdOrElseThrow(Long productId, Long restaurantId) throws EntityNotFoundException {
        return repository.findByIdAndRestaurantId(productId, restaurantId).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("product.restaurant.not.found", productId, restaurantId)));
    }
}