package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.UserGroup;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends CustomJpaRepository<UserGroup, Long> {

}