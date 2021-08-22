package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.Permission;

import java.util.List;

public interface PermissionRepository {
    List<Permission> list();
    Permission get(Long id);
    Permission add(Permission permission);
    void remove(Long id);
}
