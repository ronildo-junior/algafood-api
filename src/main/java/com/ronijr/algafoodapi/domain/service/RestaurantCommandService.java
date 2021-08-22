package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.exception.CuisineNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRequiredPropertyEmptyException;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import com.ronijr.algafoodapi.domain.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RestaurantCommandService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CuisineQueryService cuisineQueryService;

    public Restaurant create(Restaurant restaurant) throws EntityRequiredPropertyEmptyException,
            CuisineNotFoundException {
        return update(restaurant);
    }

    public Restaurant update(Restaurant restaurant) throws EntityRequiredPropertyEmptyException,
            CuisineNotFoundException {
        Long cuisineId = Optional.ofNullable(restaurant.getCuisine()).
                map(Cuisine::getId).
                orElseThrow(() ->
                        new EntityRequiredPropertyEmptyException("Cuisine with id is required."));
        Cuisine cuisine = cuisineQueryService.findById(cuisineId);
        restaurant.setCuisine(cuisine);
        return restaurantRepository.save(restaurant);
    }

    public void delete(Long id) throws EntityNotFoundException {
        try {
            restaurantRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(id);
        }
    }
}
