package com.ronijr.algafoodapi.domain.service.command;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.Product;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import com.ronijr.algafoodapi.domain.repository.ProductRepository;
import com.ronijr.algafoodapi.domain.service.query.RestaurantQuery;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class ProductCommand {
    private final ProductRepository repository;
    private final RestaurantQuery restaurantQuery;
    private final AppMessageSource messageSource;

    public Product create(Long restaurantId, Product product) {
        Restaurant restaurant = restaurantQuery.findByIdOrElseThrow(restaurantId);
        product.setRestaurant(restaurant);
        return repository.save(product);
    }

    public Product update(Long productId, Long restaurantId, Product product) {
        Restaurant restaurant = restaurantQuery.findByIdOrElseThrow(restaurantId);
        product.setRestaurant(restaurant);
        Product current = findById(productId, restaurantId);
        BeanUtils.copyProperties(product, current, "id");
        return repository.save(current);
    }

    public void delete(Long productId, Long restaurantId) {
        Product product = findById(productId, restaurantId);
        repository.delete(product);
    }

    public void activate(Long productId, Long restaurantId) throws EntityNotFoundException {
        Product product = findById(productId, restaurantId);
        product.activate();
    }

    public void inactivate(Long productId, Long restaurantId) throws EntityNotFoundException {
        Product product = findById(productId, restaurantId);
        product.inactivate();
    }

    private Product findById(Long productId, Long restaurantId) throws EntityNotFoundException {
        return repository.findByIdAndRestaurantId(productId, restaurantId).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("product.restaurant.not.found", productId, restaurantId)));
    }
}