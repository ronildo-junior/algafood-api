package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.PaymentMethodModel;
import com.ronijr.algafoodapi.config.mapper.PaymentMethodMapper;
import com.ronijr.algafoodapi.domain.model.PaymentMethod;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PaymentMethodDisassembler {
    private final PaymentMethodMapper mapper;

    public PaymentMethod toDomain(PaymentMethodModel.Input input) {
        return mapper.inputToEntity(input);
    }

    public void copyToDomainObject(PaymentMethodModel.Input input, PaymentMethod paymentMethod) {
        mapper.mergeIntoTarget(input, paymentMethod);
    }
}