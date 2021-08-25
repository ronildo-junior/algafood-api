package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRequiredPropertyEmptyException;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import com.ronijr.algafoodapi.domain.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RestaurantCommandService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CuisineQueryService cuisineQueryService;

    public Restaurant create(Restaurant restaurant) throws EntityRequiredPropertyEmptyException,
            EntityNotFoundException, EntityRelationshipNotFoundException {
        return update(restaurant);
    }

    public Restaurant update(Restaurant restaurant) throws EntityRequiredPropertyEmptyException,
            EntityNotFoundException, EntityRelationshipNotFoundException {
        Long cuisineId = Optional.ofNullable(restaurant.getCuisine()).
                map(Cuisine::getId).
                orElseThrow(() ->
                        new EntityRequiredPropertyEmptyException("Cuisine with id is required."));
        Cuisine cuisine = cuisineQueryService.findById(cuisineId).
                orElseThrow(() ->
                        new EntityRelationshipNotFoundException(
                                String.format("Cuisine with id %d not found.", cuisineId)));
        restaurant.setCuisine(cuisine);
        return restaurantRepository.save(restaurant);
    }

    public void delete(Long id) throws EntityRelationshipException, EntityNotFoundException {
        try {
            Restaurant restaurant = findById(id);
            restaurantRepository.delete(restaurant);
        } catch (DataIntegrityViolationException e) {
            throw new EntityRelationshipException(String.format("Restaurant with id %d can not be deleted.", id));
        }
    }

    private Restaurant findById(Long id) throws EntityNotFoundException {
        return restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("Restaurant with id %d not found.", id)));
    }
}