package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.RestaurantModel;
import com.ronijr.algafoodapi.config.mapper.RestaurantMapper;
import com.ronijr.algafoodapi.domain.model.Address;
import com.ronijr.algafoodapi.domain.model.City;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RestaurantDisassembler {
    private final RestaurantMapper mapper;

    public Restaurant toDomain(RestaurantModel.Input input) {
        return mapper.inputToEntity(input);
    }

    /**
     * Its necessary detach nested entity or replace by new entity non managed by hibernate,
     * to avoid invoke entity manager in this context, I choose set New Entity.
     * */
    public void copyToDomainObject(RestaurantModel.Input input, Restaurant restaurant) {
        restaurant.setCuisine(new Cuisine());
        Address address = restaurant.getAddress();
        address.setCity(new City());
        mapper.mergeIntoTarget(input, restaurant);
    }
}