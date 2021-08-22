package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.PaymentMethod;

import java.util.List;

public interface PaymentMethodRepository {
    List<PaymentMethod> list();
    PaymentMethod get(Long id);
    PaymentMethod add(PaymentMethod paymentMethod);
    void remove(Long id);
}
