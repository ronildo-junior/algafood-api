package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRequiredPropertyEmptyException;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import com.ronijr.algafoodapi.domain.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class RestaurantCommandService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CuisineQueryService cuisineQueryService;

    public Restaurant create(Restaurant restaurant){
        if (restaurant.getCuisine() == null || restaurant.getCuisine().getId() == null) {
            throw new EntityRequiredPropertyEmptyException("Cuisine with id is required");
        }
        Long cuisineId = restaurant.getCuisine().getId();
        Cuisine cuisine = cuisineQueryService.findById(cuisineId);
        if (cuisine == null) {
            throw new EntityNotFoundException(String.format("Cuisine with id %d not found", cuisineId));
        }
        restaurant.setCuisine(cuisine);
        return restaurantRepository.add(restaurant);
    }

    public Restaurant update(Restaurant restaurant){
        if (restaurant.getCuisine() == null || restaurant.getCuisine().getId() == null) {
            throw new EntityRequiredPropertyEmptyException("Cuisine with id is required");
        }
        Long cuisineId = restaurant.getCuisine().getId();
        Cuisine cuisine = cuisineQueryService.findById(cuisineId);
        if (cuisine == null) {
            throw new EntityNotFoundException(String.format("Cuisine with id %d not found", cuisineId));
        }
        try {
            restaurant.setCuisine(cuisine);
            return restaurantRepository.add(restaurant);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Restaurant with id %d not found", restaurant.getId()));
        }
    }

    public void delete(Long id){
        try {
            restaurantRepository.remove(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(id);
        }
    }
}
