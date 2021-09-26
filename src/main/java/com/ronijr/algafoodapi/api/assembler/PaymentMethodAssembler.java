package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.controller.PaymentMethodController;
import com.ronijr.algafoodapi.api.model.PaymentMethodModel;
import com.ronijr.algafoodapi.config.mapper.PaymentMethodMapper;
import com.ronijr.algafoodapi.domain.model.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PaymentMethodAssembler extends RepresentationModelAssemblerSupport<PaymentMethod, PaymentMethodModel.Output> {
    @Autowired
    private PaymentMethodMapper mapper;

    public PaymentMethodAssembler() {
        super(PaymentMethod.class, PaymentMethodModel.Output.class);
    }

    @Override
    public PaymentMethodModel.Output toModel(PaymentMethod paymentMethod) {
        PaymentMethodModel.Output model = mapper.entityToOutput(paymentMethod);
        model.add(linkTo(methodOn(PaymentMethodController.class).get(paymentMethod.getId())).withSelfRel());
        model.add(linkTo(methodOn(PaymentMethodController.class).list()).withRel("payment-method-list"));
        return model;
    }

    @Override
    public CollectionModel<PaymentMethodModel.Output> toCollectionModel(Iterable<? extends PaymentMethod> paymentMethods) {
        return super.toCollectionModel(paymentMethods)
                .add(linkTo(PaymentMethodController.class).withSelfRel());
    }
}