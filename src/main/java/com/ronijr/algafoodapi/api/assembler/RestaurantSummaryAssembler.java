package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.controller.CuisineController;
import com.ronijr.algafoodapi.api.controller.RestaurantController;
import com.ronijr.algafoodapi.api.model.RestaurantModel;
import com.ronijr.algafoodapi.config.mapper.RestaurantMapper;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RestaurantSummaryAssembler extends RepresentationModelAssemblerSupport<Restaurant, RestaurantModel.Summary> {
    @Autowired
    private RestaurantMapper mapper;

    public RestaurantSummaryAssembler() {
        super(Restaurant.class, RestaurantModel.Summary.class);
    }

    @Override
    public RestaurantModel.Summary toModel(Restaurant restaurant) {
        RestaurantModel.Summary model = mapper.entityToSummary(restaurant);
        model.add(linkTo(methodOn(RestaurantController.class).get(restaurant.getId())).withSelfRel());
        model.add(linkTo(methodOn(RestaurantController.class).list(null)).withRel("restaurant-list"));
        model.getCuisine().add(linkTo(methodOn(CuisineController.class).get(restaurant.getCuisine().getId())).withSelfRel());
        return model;
    }

    @Override
    public CollectionModel<RestaurantModel.Summary> toCollectionModel(Iterable<? extends Restaurant> cities) {
        return super.toCollectionModel(cities)
                .add(linkTo(RestaurantController.class).withSelfRel());
    }
}