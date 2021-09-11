package com.ronijr.algafoodapi.domain.service.command;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipException;
import com.ronijr.algafoodapi.domain.exception.ValidationException;
import com.ronijr.algafoodapi.domain.model.PaymentMethod;
import com.ronijr.algafoodapi.domain.repository.PaymentMethodRepository;
import com.ronijr.algafoodapi.domain.validation.ResourceValidator;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class PaymentMethodCommand {
    private final PaymentMethodRepository paymentMethodRepository;
    private final AppMessageSource messageSource;
    private final ResourceValidator validator;

    public PaymentMethod create(PaymentMethod paymentMethod) throws ValidationException {
        return update(paymentMethod);
    }

    public PaymentMethod update(PaymentMethod paymentMethod) throws ValidationException {
        validator.validate(paymentMethod);
        return paymentMethodRepository.save(paymentMethod);
    }

    public void delete(Long id) throws EntityRelationshipException, EntityNotFoundException {
        try {
            PaymentMethod paymentMethod = findById(id);
            paymentMethodRepository.delete(paymentMethod);
            paymentMethodRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntityRelationshipException(messageSource.getMessage("payment.method.relationship.found", id));
        }
    }

    private PaymentMethod findById(Long id) throws EntityNotFoundException {
        return paymentMethodRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage("payment.method.not.found", id)));
    }
}