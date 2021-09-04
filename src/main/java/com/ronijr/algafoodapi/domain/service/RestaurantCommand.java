package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipNotFoundException;
import com.ronijr.algafoodapi.domain.exception.ValidationException;
import com.ronijr.algafoodapi.domain.model.Address;
import com.ronijr.algafoodapi.domain.model.City;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import com.ronijr.algafoodapi.domain.model.Restaurant;
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

    public void activate(Long id) throws EntityNotFoundException {
        Restaurant restaurant = findById(id);
        restaurant.activate();
    }

    public void inactivate(Long id) throws EntityNotFoundException {
        Restaurant restaurant = findById(id);
        restaurant.inactivate();
    }

    private Restaurant findById(Long id) throws EntityNotFoundException {
        return restaurantRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage("restaurant.not.found", id)));
    }
}