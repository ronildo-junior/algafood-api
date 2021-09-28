package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.RestaurantModel;
import com.ronijr.algafoodapi.config.mapper.RestaurantMapper;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.api.hateoas.AlgaLinks.*;

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
        model.add(linkToRestaurant(restaurant.getId()));
        model.add(linkToRestaurants("restaurant-list"));

        model.getCuisine().add(linkToCuisine(restaurant.getCuisine().getId()));
        if (restaurant.getAddress() != null && restaurant.getAddress().getCity() != null) {
            model.getAddress().getCity().add(linkToCity(restaurant.getAddress().getCity().getId()));
        }

        model.add(linkToRestaurantPaymentMethods(restaurant.getId(), "payment-method-list"));
        model.add(linkToManagersRestaurant(restaurant.getId(), "user-manager-list"));
        model.add(linkToProducts(restaurant.getId(), "product-list"));

        if (restaurant.canActivate()) {
            model.add(linkToRestaurantActivation(restaurant.getId(), "activate"));
        }
        if (restaurant.canInactivate()) {
            model.add(linkToRestaurantInactivation(restaurant.getId(), "inactivate"));
        }
        if (restaurant.canOpen()) {
            model.add(linkToRestaurantOpening(restaurant.getId(), "open"));
        }
        if (restaurant.canClose()) {
            model.add(linkToRestaurantClosing(restaurant.getId(), "close"));
        }
        return model;
    }
}