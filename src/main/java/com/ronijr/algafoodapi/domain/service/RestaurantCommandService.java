package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRequiredPropertyEmptyException;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import com.ronijr.algafoodapi.domain.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RestaurantCommandService {
    private final RestaurantRepository restaurantRepository;
    private final CuisineQueryService cuisineQueryService;
    private final AppMessageSource messageSource;

    public Restaurant create(Restaurant restaurant) throws EntityRequiredPropertyEmptyException,
            EntityNotFoundException, EntityRelationshipNotFoundException {
        return update(restaurant);
    }

    public Restaurant update(Restaurant restaurant) throws EntityRequiredPropertyEmptyException,
            EntityNotFoundException, EntityRelationshipNotFoundException {
        Long cuisineId = Optional.ofNullable(restaurant.getCuisine()).
                map(Cuisine::getId).
                orElseThrow(() ->
                        new EntityRequiredPropertyEmptyException(
                                messageSource.getMessage("cuisine.not.empty")));
        Cuisine cuisine = cuisineQueryService.findById(cuisineId).
                orElseThrow(() ->
                        new EntityRelationshipNotFoundException(
                                messageSource.getMessage("cuisine.not.found", new Object[] { cuisineId } )));
        restaurant.setCuisine(cuisine);
        return restaurantRepository.save(restaurant);
    }

    public void delete(Long id) throws EntityRelationshipException, EntityNotFoundException {
        try {
            Restaurant restaurant = findById(id);
            restaurantRepository.delete(restaurant);
        } catch (DataIntegrityViolationException e) {
            throw new EntityRelationshipException(
                    messageSource.getMessage("restaurant.relationship.found", new Object[] { id }));
        }
    }

    private Restaurant findById(Long id) throws EntityNotFoundException {
        return restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("restaurant.not.found", new Object[] { id })));
    }
}