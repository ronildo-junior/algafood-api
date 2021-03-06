package com.ronijr.algafoodapi.api.v1.model.mixin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ronijr.algafoodapi.domain.model.City;

public abstract class AddressMixin {
    @JsonIgnoreProperties(value = "name", allowGetters = true)
    private City city;
}