package com.ronijr.algafoodapi.api.v1.model;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;

public final class PermissionModel {
    private interface Name { @NotBlank String getName(); }
    private interface Description { @NotBlank String getDescription(); }

    @Value
    public static class Input implements Name, Description {
        String name;
        String description;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    public static class Output extends RepresentationModel<Output> {
        Long id;
        String name;
        String description;
    }
}