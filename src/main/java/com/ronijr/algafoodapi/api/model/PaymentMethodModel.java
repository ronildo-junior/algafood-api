package com.ronijr.algafoodapi.api.model;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public final class PaymentMethodModel {
    private interface Id { @Positive Long getId(); }
    private interface Description { @NotBlank String getDescription(); }

    @Value
    public static class Identifier implements Id  {
        Long id;
    }

    @Value
    public static class Input implements Description {
        String description;
    }

   @Value
    public static class Output implements Id, Description {
        Long id;
        String description;
    }
}