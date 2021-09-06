package com.ronijr.algafoodapi.api.model;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public final class ProductModel {
    private interface Id { @NotNull @Positive Long getId(); }
    private interface Name { @NotBlank String getName(); }
    private interface Description { @NotBlank String getDescription(); }
    private interface Price { @NotNull @PositiveOrZero BigDecimal getPrice(); }

    @Value
    public static class Input implements Name, Description, Price {
        String name;
        String description;
        BigDecimal price;
    }

    @Value
    public static class Output implements Id, Name, Description, Price {
        Long id;
        String name;
        String description;
        BigDecimal price;
    }
}