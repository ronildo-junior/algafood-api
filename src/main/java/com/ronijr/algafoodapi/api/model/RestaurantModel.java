package com.ronijr.algafoodapi.api.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.ronijr.algafoodapi.api.model.view.RestaurantView;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public final class RestaurantModel {
    private interface Id { @NotNull @Positive Long getId(); }
    private interface Name { @NotBlank String getName(); }
    private interface DeliveryFee { @NotNull @PositiveOrZero BigDecimal getDeliveryFee(); }
    private interface CuisineIdentifier { @NotNull @Valid CuisineModel.Identifier getCuisine(); }
    private interface AddressInput { @NotNull @Valid AddressModel.Input getAddress(); }

    @Value
    public static class Identifier implements Id {
        Long id;
    }

    @Value
    public static class Input implements Name, DeliveryFee, CuisineIdentifier, AddressInput {
        String name;
        BigDecimal deliveryFee;
        CuisineModel.Identifier cuisine;
        AddressModel.Input address;
    }

    @Value
    public static class Output {
        Long id;
        String name;
        BigDecimal deliveryFee;
        Boolean active;
        Boolean opened;
        CuisineModel.Output cuisine;
        AddressModel.Output address;
    }

    @Value
    public static class Summary {
        Long id;
        @JsonView(RestaurantView.OnlyName.class)
        String name;
        BigDecimal deliveryFee;
        Boolean active;
        Boolean opened;
        CuisineModel.Output cuisine;
    }

    @Value
    public static class Simple {
        Long id;
        String name;
    }
}