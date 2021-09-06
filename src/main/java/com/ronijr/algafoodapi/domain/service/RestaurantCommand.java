package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipNotFoundException;
import com.ronijr.algafoodapi.domain.exception.ValidationException;
import com.ronijr.algafoodapi.domain.model.*;
import com.ronijr.algafoodapi.domain.repository.RestaurantRepository;
import com.ronijr.algafoodapi.domain.validation.ResourceValidator;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class RestaurantCommand {
    private final RestaurantRepository restaurantRepository;
    private final CuisineQuery cuisineQuery;
    private final CityQuery cityQuery;
    private final ProductCommand productCommand;
    private final PaymentMethodQuery paymentMethodQuery;
    private final AppMessageSource messageSource;
    private final ResourceValidator validator;

    public Restaurant create(Restaurant restaurant) throws ValidationException,
            EntityNotFoundException, EntityRelationshipNotFoundException {
        return update(restaurant);
    }

    public Restaurant update(Restaurant restaurant) throws ValidationException,
            EntityNotFoundException, EntityRelationshipNotFoundException {
        validator.validate(restaurant);
        Long cuisineId = restaurant.getCuisine().getId();
        Cuisine cuisine = cuisineQuery.findById(cuisineId).
                orElseThrow(() -> new EntityRelationshipNotFoundException(
                        messageSource.getMessage("cuisine.not.found", cuisineId)));
        Long cityId = restaurant.getAddress().getCity().getId();
        City city = cityQuery.findById(cityId).
                orElseThrow(() -> new EntityRelationshipNotFoundException(
                        messageSource.getMessage("city.not.found", cityId)));
        Address address = restaurant.getAddress();
        address.setCity(city);
        restaurant.setAddress(address);
        restaurant.setCuisine(cuisine);
        return restaurantRepository.save(restaurant);
    }

    public void delete(Long id) throws EntityRelationshipException, EntityNotFoundException {
        try {
            Restaurant restaurant = findById(id);
            restaurantRepository.delete(restaurant);
            restaurantRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntityRelationshipException(messageSource.getMessage("restaurant.relationship.found", id));
        }
    }

    public void activateRestaurant(Long id) throws EntityNotFoundException {
        Restaurant restaurant = findById(id);
        restaurant.activate();
    }

    public void inactivateRestaurant(Long id) throws EntityNotFoundException {
        Restaurant restaurant = findById(id);
        restaurant.inactivate();
    }

    public void associatePaymentMethod(Long restaurantId, Long paymentMethodId) {
        Restaurant restaurant = findById(restaurantId);
        PaymentMethod paymentMethod1 = paymentMethodQuery.findByIdOrElseThrow(paymentMethodId);
        if (!restaurant.hasPaymentMethod(paymentMethod1)) {
            restaurant.addPaymentMethod(paymentMethod1);
        }
    }

    public void disassociatePaymentMethod(Long restaurantId, Long paymentMethodId) {
        Restaurant restaurant = findById(restaurantId);
        PaymentMethod paymentMethod1 = paymentMethodQuery.findByIdOrElseThrow(paymentMethodId);
        if (restaurant.hasPaymentMethod(paymentMethod1)) {
            restaurant.removePaymentMethod(paymentMethod1);
        }
    }

    public Product createProduct(Long restaurantId, Product product) {
        Restaurant restaurant = findById(restaurantId);
        product.setRestaurant(restaurant);
        Product saved = productCommand.create(product);
        restaurant.addProduct(saved);
        return saved;
    }

    public Product updateProduct(Long restaurantId, Long productId, Product product) {
        Restaurant restaurant = findById(restaurantId);
        product.setRestaurant(restaurant);
        Product saved = productCommand.update(productId, restaurantId, product);
        restaurant.addProduct(saved);
        return saved;
    }

    public void activateProduct(Long productId, Long restaurantId) throws EntityNotFoundException {
        productCommand.activate(productId, restaurantId);
    }

    public void inactivateProduct(Long productId, Long restaurantId) throws EntityNotFoundException {
        productCommand.inactivate(productId, restaurantId);
    }

    private Restaurant findById(Long id) throws EntityNotFoundException {
        return restaurantRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage("restaurant.not.found", id)));
    }
}