package com.ronijr.algafoodapi.api.v1.assembler;

import com.ronijr.algafoodapi.api.v1.model.RestaurantModel;
import com.ronijr.algafoodapi.config.mapper.RestaurantMapper;
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

    public void copyToDomainObject(RestaurantModel.Input input, Restaurant restaurant) {
        mapper.mergeIntoTarget(input, restaurant);
    }
}