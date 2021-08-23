package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.State;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends CustomJpaRepository<State, Long> {

}
