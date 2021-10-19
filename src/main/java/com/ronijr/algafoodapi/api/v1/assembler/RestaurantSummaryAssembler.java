package com.ronijr.algafoodapi.api.v1.assembler;

import com.ronijr.algafoodapi.api.v1.model.RestaurantModel;
import com.ronijr.algafoodapi.config.mapper.RestaurantMapper;
import com.ronijr.algafoodapi.config.security.AlgaSecurity;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.*;

@Component
public class RestaurantSummaryAssembler extends RepresentationModelAssemblerSupport<Restaurant, RestaurantModel.Summary> {
    @Autowired
    private RestaurantMapper mapper;
    @Autowired
    private AlgaSecurity algaSecurity;

    public RestaurantSummaryAssembler() {
        super(Restaurant.class, RestaurantModel.Summary.class);
    }

    @Override
    public RestaurantModel.Summary toModel(Restaurant restaurant) {
        RestaurantModel.Summary model = mapper.entityToSummary(restaurant);
        model.add(linkToRestaurant(restaurant.getId()));
        if (algaSecurity.allowQueryRestaurants()) {
            model.add(linkToRestaurants("restaurant-list"));
        }
        if (algaSecurity.allowQueryCuisines()) {
            model.getCuisine().add(linkToCuisine(restaurant.getCuisine().getId()));
        }
        return model;
    }

    @Override
    public CollectionModel<RestaurantModel.Summary> toCollectionModel(Iterable<? extends Restaurant> cities) {
        return super.toCollectionModel(cities).add(linkToRestaurants());
    }
}