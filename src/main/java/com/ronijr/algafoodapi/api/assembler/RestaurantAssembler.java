package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.RestaurantModel;
import com.ronijr.algafoodapi.config.mapper.RestaurantMapper;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RestaurantAssembler {
    private final RestaurantMapper mapper;

    public RestaurantModel.Summary toSummary(Restaurant restaurant) {
        return mapper.entityToSummary(restaurant);
    }

    public RestaurantModel.Output toOutput(Restaurant restaurant) {
        return mapper.entityToOutput(restaurant);
    }

    public RestaurantModel.Input toInput(Restaurant restaurant) {
        return mapper.entityToInput(restaurant);
    }

    public List<RestaurantModel.Summary> toCollectionModel(List<Restaurant> cities) {
        return cities.stream().
                map(this::toSummary).
                collect(Collectors.toList());
    }
}