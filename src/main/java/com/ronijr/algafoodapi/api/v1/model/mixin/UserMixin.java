package com.ronijr.algafoodapi.api.v1.model.mixin;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class UserMixin {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}