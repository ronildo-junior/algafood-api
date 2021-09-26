package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.controller.CityController;
import com.ronijr.algafoodapi.api.controller.CuisineController;
import com.ronijr.algafoodapi.api.controller.RestaurantController;
import com.ronijr.algafoodapi.api.model.RestaurantModel;
import com.ronijr.algafoodapi.config.mapper.RestaurantMapper;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RestaurantAssembler extends RepresentationModelAssemblerSupport<Restaurant, RestaurantModel.Output> {
    @Autowired
    private RestaurantMapper mapper;

    public RestaurantAssembler() {
        super(Restaurant.class, RestaurantModel.Output.class);
    }

    @Override
    public RestaurantModel.Output toModel(Restaurant restaurant) {
        RestaurantModel.Output model = mapper.entityToOutput(restaurant);
        model.add(linkTo(methodOn(RestaurantController.class).get(restaurant.getId())).withSelfRel());
        model.add(linkTo(methodOn(RestaurantController.class).list(null)).withRel("restaurant-list"));
        model.getCuisine().add(linkTo(methodOn(CuisineController.class).get(restaurant.getCuisine().getId())).withSelfRel());
        model.getAddress().getCity().add(linkTo(methodOn(CityController.class)
                .get(restaurant.getAddress().getCity().getId()))
                .withSelfRel());
        return model;
    }
}