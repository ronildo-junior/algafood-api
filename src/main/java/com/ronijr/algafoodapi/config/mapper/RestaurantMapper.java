package com.ronijr.algafoodapi.config.mapper;

import com.ronijr.algafoodapi.api.model.RestaurantResource;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    Restaurant inputToEntity(RestaurantResource.Input input);
    RestaurantResource.Output entityToOutput(Restaurant entity);
    RestaurantResource.Summary entityToSummary(Restaurant entity);
}