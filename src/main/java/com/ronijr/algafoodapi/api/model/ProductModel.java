package com.ronijr.algafoodapi.api.model;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public final class ProductModel {
    private interface Name { @NotBlank String getName(); }
    private interface Description { @NotBlank String getDescription(); }
    private interface Price { @NotNull @PositiveOrZero BigDecimal getPrice(); }

    @Value
    public static class Input implements Name, Description, Price {
        String name;
        String description;
        BigDecimal price;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    public static class Output extends RepresentationModel<Output> {
        Long id;
        String name;
        String description;
        BigDecimal price;
    }
}