package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import com.ronijr.algafoodapi.domain.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantQueryService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<Restaurant> findAll(){
        return restaurantRepository.list();
    }

    public Restaurant findById(Long id) {
        try {
            return restaurantRepository.get(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(id);
        }
    }
}
