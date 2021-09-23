package com.ronijr.algafoodapi.api.model;

import lombok.Value;

import javax.validation.constraints.NotBlank;

public final class PermissionModel {
    private interface Name { @NotBlank String getName(); }
    private interface Description { @NotBlank String getDescription(); }

    @Value
    public static class Input implements Name, Description {
        String name;
        String description;
    }

    @Value
    public static class Output {
        Long id;
        String name;
        String description;
    }
}