package com.ronijr.algafoodapi.config.mapper;

import com.ronijr.algafoodapi.api.v1.model.RestaurantModel;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    Restaurant inputToEntity(RestaurantModel.Input input);
    RestaurantModel.Output entityToOutput(Restaurant entity);
    RestaurantModel.Input entityToInput(Restaurant entity);
    RestaurantModel.Summary entityToSummary(Restaurant entity);
    void mergeIntoTarget(RestaurantModel.Input input, @MappingTarget Restaurant target);
}