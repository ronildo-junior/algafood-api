package com.ronijr.algafoodapi.api.model.mixin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ronijr.algafoodapi.domain.model.Restaurant;

import java.util.List;

public abstract class CuisineMixin {
    @JsonIgnore
    private List<Restaurant> restaurants;
}