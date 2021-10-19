package com.ronijr.algafoodapi.api.v1.assembler;

import com.ronijr.algafoodapi.api.v1.model.PaymentMethodModel;
import com.ronijr.algafoodapi.config.mapper.PaymentMethodMapper;
import com.ronijr.algafoodapi.config.security.AlgaSecurity;
import com.ronijr.algafoodapi.domain.model.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.linkToPaymentMethod;
import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.linkToPaymentMethods;

@Component
public class PaymentMethodAssembler extends RepresentationModelAssemblerSupport<PaymentMethod, PaymentMethodModel.Output> {
    @Autowired
    private PaymentMethodMapper mapper;
    @Autowired
    private AlgaSecurity algaSecurity;

    public PaymentMethodAssembler() {
        super(PaymentMethod.class, PaymentMethodModel.Output.class);
    }

    @Override
    public PaymentMethodModel.Output toModel(PaymentMethod paymentMethod) {
        PaymentMethodModel.Output model = mapper.entityToOutput(paymentMethod);
        model.add(linkToPaymentMethod(paymentMethod.getId()));
        if (algaSecurity.allowQueryPaymentMethods()) {
            model.add(linkToPaymentMethods("payment-method-list"));
        }
        return model;
    }

    @Override
    public CollectionModel<PaymentMethodModel.Output> toCollectionModel(Iterable<? extends PaymentMethod> paymentMethods) {
        return super.toCollectionModel(paymentMethods).add(linkToPaymentMethods());
    }
}