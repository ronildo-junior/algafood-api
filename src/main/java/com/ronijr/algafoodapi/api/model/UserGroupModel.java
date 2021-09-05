package com.ronijr.algafoodapi.api.model;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public final class UserGroupModel {
    private interface Id { @Positive Long getId(); }
    private interface Name { @NotBlank String getName(); }

    @Value
    public static class Identifier implements Id  {
        Long id;
    }

    @Value
    public static class Input implements Name {
        String name;
    }

   @Value
    public static class Output implements Id, Name {
        Long id;
        String name;
    }
}