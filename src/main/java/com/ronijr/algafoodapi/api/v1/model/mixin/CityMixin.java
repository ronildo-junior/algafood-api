package com.ronijr.algafoodapi.api.v1.model.mixin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ronijr.algafoodapi.domain.model.State;

public abstract class CityMixin {
    @JsonIgnoreProperties(value = "name", allowGetters = true)
    private State state;
}