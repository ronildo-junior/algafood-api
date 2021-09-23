package com.ronijr.algafoodapi.api.model;

import lombok.Value;

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

   @Value
    public static class Output {
        Long id;
        String description;
    }
}