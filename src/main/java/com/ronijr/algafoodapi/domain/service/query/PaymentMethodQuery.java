package com.ronijr.algafoodapi.domain.service.query;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.PaymentMethod;
import com.ronijr.algafoodapi.domain.repository.PaymentMethodRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentMethodQuery {
    private final PaymentMethodRepository paymentMethodRepository;
    private final AppMessageSource messageSource;

    public List<PaymentMethod> findAll() {
        return paymentMethodRepository.findAll();
    }

    public Optional<PaymentMethod> findById(Long id) {
        return paymentMethodRepository.findById(id);
    }

    public PaymentMethod findByIdOrElseThrow(Long id) throws EntityNotFoundException {
        return paymentMethodRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("payment.method.not.found", id)));
    }
}