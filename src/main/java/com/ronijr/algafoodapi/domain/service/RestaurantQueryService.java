package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import com.ronijr.algafoodapi.domain.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ronijr.algafoodapi.infrastructure.repository.specification.RestaurantSpecification.withFreeDelivery;

@Service
public class RestaurantQueryService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public Optional<Restaurant> findById(Long id) throws EntityNotFoundException {
        return restaurantRepository.findById(id);
    }

    public Restaurant findByIdOrElseThrow(Long id) throws EntityNotFoundException {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurant", id.toString()));
    }

    public List<Restaurant> customQuery(Map<String, Object> parameters){
        return restaurantRepository.findCriteriaTest(parameters);
    }

    public List<Restaurant> findAllWithFreeDelivery(){
        return restaurantRepository.findAll(withFreeDelivery());
    }

    public Restaurant findFirst() throws EntityNotFoundException {
        return restaurantRepository.findFirst().orElseThrow(() -> new EntityNotFoundException("No Restaurant found"));
    }
}