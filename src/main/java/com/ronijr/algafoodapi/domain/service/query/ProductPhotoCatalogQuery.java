package com.ronijr.algafoodapi.domain.service.query;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.ProductPhoto;
import com.ronijr.algafoodapi.domain.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ProductPhotoCatalogQuery {
    private final ProductRepository productRepository;
    private final AppMessageSource messageSource;

    public ProductPhoto findByIdAndRestaurantId(Long productId, Long restaurantId) {
        if (!productRepository.existsProductWithIdAndRestaurantId(productId, restaurantId)) {
            throw new EntityNotFoundException(messageSource.getMessage("product.restaurant.not.found", productId, productId));
        }
        return productRepository.findPhotoByIdAndRestaurantId(productId, restaurantId).orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage("product.photo.not.found", productId, productId)));
    }
}