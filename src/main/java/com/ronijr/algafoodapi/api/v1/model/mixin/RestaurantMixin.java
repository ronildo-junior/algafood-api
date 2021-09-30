package com.ronijr.algafoodapi.api.v1.model.mixin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ronijr.algafoodapi.domain.model.Address;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import com.ronijr.algafoodapi.domain.model.PaymentMethod;
import com.ronijr.algafoodapi.domain.model.Product;

import java.util.ArrayList;
import java.util.List;

public abstract class RestaurantMixin {
    @JsonIgnoreProperties(value = "name", allowGetters = true)
    private Cuisine cuisine;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Address address;

    @JsonIgnore
    private final List<PaymentMethod> paymentMethods = new ArrayList<>();

    @JsonIgnore
    private final List<Product> products = new ArrayList<>();
}