package com.ronijr.algafoodapi.api.model;

import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public final class RestaurantModel {
    private interface Id { @Positive Long getId(); }
    private interface Name { @NotBlank String getName(); }
    private interface DeliveryFee { @NotNull @PositiveOrZero BigDecimal getDeliveryFee(); }
    private interface CuisineIdentifier { @NotNull @Valid CuisineModel.Identifier getCuisine(); }
    private interface CuisineOutput { @NotNull CuisineModel.Output getCuisine(); }
    private interface AddressOutput { @NotNull AddressModel.Output getAddress(); }
    private interface AddressInput { @NotNull @Valid AddressModel.Input getAddress(); }

    @Value
    public static class Input implements Name, DeliveryFee, CuisineIdentifier, AddressInput {
        String name;
        BigDecimal deliveryFee;
        CuisineModel.Identifier cuisine;
        AddressModel.Input address;
    }

    @Value
    public static class Output implements Id, Name, DeliveryFee, CuisineOutput, AddressOutput {
        Long id;
        String name;
        BigDecimal deliveryFee;
        CuisineModel.Output cuisine;
        AddressModel.Output address;
    }

    @Value
    public static class Summary implements Id, Name, DeliveryFee, CuisineOutput {
        Long id;
        String name;
        BigDecimal deliveryFee;
        CuisineModel.Output cuisine;
    }
}