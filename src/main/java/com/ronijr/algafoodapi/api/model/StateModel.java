package com.ronijr.algafoodapi.api.model;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public final class StateModel {
    private interface Id { @Positive Long getId(); }
    private interface Name { @NotBlank String getName(); }
    private interface Abbreviation { @NotBlank String getAbbreviation(); }

    @Value
    public static class Identifier implements Id  {
        Long id;
    }

    @Value
    public static class Input implements Name {
        String name;
    }

   @Value
    public static class Output implements Id, Name, Abbreviation {
        Long id;
        String name;
        String abbreviation;
    }

    @Value
    public static class Summary implements Name, Abbreviation {
        String name;
        String abbreviation;
    }
}