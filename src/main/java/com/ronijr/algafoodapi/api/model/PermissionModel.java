package com.ronijr.algafoodapi.api.model;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public final class PermissionModel {
    private interface Id { @NotNull @Positive Long getId(); }
    private interface Name { @NotBlank String getName(); }
    private interface Description { @NotBlank String getDescription(); }

    @Value
    public static class Input implements Name, Description {
        String name;
        String description;
    }

    @Value
    public static class Output implements Id, Name, Description {
        Long id;
        String name;
        String description;
    }
}