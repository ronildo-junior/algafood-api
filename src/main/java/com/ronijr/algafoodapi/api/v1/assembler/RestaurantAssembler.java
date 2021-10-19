package com.ronijr.algafoodapi.api.v1.assembler;

import com.ronijr.algafoodapi.api.v1.model.RestaurantModel;
import com.ronijr.algafoodapi.config.mapper.RestaurantMapper;
import com.ronijr.algafoodapi.config.security.AlgaSecurity;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.*;

@Component
public class RestaurantAssembler extends RepresentationModelAssemblerSupport<Restaurant, RestaurantModel.Output> {
    @Autowired
    private RestaurantMapper mapper;
    @Autowired
    private AlgaSecurity algaSecurity;

    public RestaurantAssembler() {
        super(Restaurant.class, RestaurantModel.Output.class);
    }

    @Override
    public RestaurantModel.Output toModel(Restaurant restaurant) {
        RestaurantModel.Output model = mapper.entityToOutput(restaurant);
        model.add(linkToRestaurant(restaurant.getId()));
        if (algaSecurity.allowEditRestaurant()) {
            if (restaurant.canActivate()) {
                model.add(linkToRestaurantActivation(restaurant.getId(), "activate"));
            }
            if (restaurant.canInactivate()) {
                model.add(linkToRestaurantInactivation(restaurant.getId(), "inactivate"));
            }
        }
        if (algaSecurity.allowManageRestaurant(restaurant.getId())) {
            model.add(linkToManagersRestaurant(restaurant.getId(), "user-manager-list"));
            if (restaurant.canOpen()) {
                model.add(linkToRestaurantOpening(restaurant.getId(), "open"));
            }
            if (restaurant.canClose()) {
                model.add(linkToRestaurantClosing(restaurant.getId(), "close"));
            }
        }
        externalLinks(model, restaurant);
        return model;
    }

    private void externalLinks(RestaurantModel.Output model, Restaurant restaurant) {
        model.getCuisine().add(linkToCuisine(restaurant.getCuisine().getId()));
        if (restaurant.getAddress() != null && restaurant.getAddress().getCity() != null) {
            model.getAddress().getCity().add(linkToCity(restaurant.getAddress().getCity().getId()));
        }
        if (algaSecurity.allowQueryPaymentMethods()) {
            model.add(linkToRestaurantPaymentMethods(restaurant.getId(), "payment-method-list"));
        }
        if (algaSecurity.allowQueryRestaurants()) {
            model.add(linkToRestaurants("restaurant-list"));
        }
        if (algaSecurity.allowQueryProducts()) {
            model.add(linkToProducts(restaurant.getId(), "product-list"));
        }
    }
}