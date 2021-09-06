package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.Product;
import com.ronijr.algafoodapi.domain.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class ProductCommand {
    private final ProductRepository repository;
    private final AppMessageSource messageSource;

    public Product create(Product product) {
        return repository.save(product);
    }

    public Product update(Long id, Long restaurantId, Product product) {
        Product current = findById(id, restaurantId);
        BeanUtils.copyProperties(product, current, "id");
        return repository.save(current);
    }

    public void delete(Long id, Long restaurantId) {
        Product product = findById(id, restaurantId);
        repository.delete(product);
    }

    public void activate(Long id, Long restaurantId) throws EntityNotFoundException {
        Product product = findById(id, restaurantId);
        product.activate();
    }

    public void inactivate(Long id, Long restaurantId) throws EntityNotFoundException {
        Product product = findById(id, restaurantId);
        product.inactivate();
    }

    private Product findById(Long id, Long restaurantId) {
        return repository.findByIdAndRestaurantId(id, restaurantId).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("product.not.found", id)));
    }
}