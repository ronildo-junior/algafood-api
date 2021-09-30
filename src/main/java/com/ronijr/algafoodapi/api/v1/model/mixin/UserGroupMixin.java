package com.ronijr.algafoodapi.api.v1.model.mixin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ronijr.algafoodapi.domain.model.Permission;

import java.util.List;

public abstract class UserGroupMixin {
    @JsonIgnore
    private List<Permission> permissions;
}