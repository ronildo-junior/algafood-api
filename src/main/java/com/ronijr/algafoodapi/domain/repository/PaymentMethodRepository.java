package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.PaymentMethod;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends CustomJpaRepository<PaymentMethod, Long> {

}
