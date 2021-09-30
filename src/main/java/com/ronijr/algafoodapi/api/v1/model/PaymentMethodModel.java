package com.ronijr.algafoodapi.api.v1.model;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public final class PaymentMethodModel {
    private interface Id { @NotNull @Positive Long getId(); }

    @Value
    public static class Identifier implements Id  {
        Long id;
    }

    @Value
    public static class Input {
        String description;
    }

   @EqualsAndHashCode(callSuper = true)
   @Value
    public static class Output extends RepresentationModel<Output> {
        Long id;
        String description;
    }
}