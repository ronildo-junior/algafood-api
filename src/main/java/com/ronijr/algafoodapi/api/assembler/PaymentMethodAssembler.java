package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.PaymentMethodModel;
import com.ronijr.algafoodapi.config.mapper.PaymentMethodMapper;
import com.ronijr.algafoodapi.domain.model.PaymentMethod;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PaymentMethodAssembler {
    private final PaymentMethodMapper mapper;

    public PaymentMethodModel.Output toOutput(PaymentMethod paymentMethod) {
        return mapper.entityToOutput(paymentMethod);
    }

    public PaymentMethodModel.Input toInput(PaymentMethod paymentMethod) {
        return mapper.entityToInput(paymentMethod);
    }

    public List<PaymentMethodModel.Output> toCollectionModel(Collection<PaymentMethod> paymentMethods) {
        return paymentMethods.stream().
                map(this::toOutput).
                collect(Collectors.toList());
    }
}