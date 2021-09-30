package com.ronijr.algafoodapi.config.mapper;

import com.ronijr.algafoodapi.api.v1.model.PaymentMethodModel;
import com.ronijr.algafoodapi.domain.model.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {
    PaymentMethod inputToEntity(PaymentMethodModel.Input input);
    PaymentMethodModel.Output entityToOutput(PaymentMethod entity);
    PaymentMethodModel.Input entityToInput(PaymentMethod entity);
    void mergeIntoTarget(PaymentMethodModel.Input input, @MappingTarget PaymentMethod target);
}