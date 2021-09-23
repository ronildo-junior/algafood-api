package com.ronijr.algafoodapi.api.model;

import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public final class CityModel {
    private interface Id { @NotNull @Positive Long getId(); }
    private interface Name { @NotBlank String getName(); }
    private interface StateIdentifier { @NotNull @Valid StateModel.Identifier getState(); }

    @Value
    public static class Identifier implements Id {
        Long id;
    }

    @Value
    public static class Input implements Name, StateIdentifier {
        String name;
        StateModel.Identifier state;
    }

    @Value
    public static class Summary {
        Long id;
        String name;
    }

    @Value
    public static class Output {
        Long id;
        String name;
        StateModel.Output state;
    }
}