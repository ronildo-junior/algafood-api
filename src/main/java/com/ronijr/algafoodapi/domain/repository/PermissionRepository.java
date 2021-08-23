package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.Permission;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends CustomJpaRepository<Permission, Long> {

}
