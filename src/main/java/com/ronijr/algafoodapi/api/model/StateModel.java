package com.ronijr.algafoodapi.api.model;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public final class StateModel {
    private interface Id { @NotNull @Positive Long getId(); }
    private interface Name { @NotBlank String getName(); }
    private interface Abbreviation { @NotBlank String getAbbreviation(); }

    @Value
    public static class Identifier implements Id  {
        Long id;
    }

    @Value
    public static class Input implements Name, Abbreviation {
        String name;
        String abbreviation;
    }

   @EqualsAndHashCode(callSuper = true)
   @Value
    public static class Output extends RepresentationModel<Output> {
        Long id;
        String name;
        String abbreviation;
    }
}