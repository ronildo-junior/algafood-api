package com.ronijr.algafoodapi.domain.validation;

import com.ronijr.algafoodapi.domain.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;

@Component
@AllArgsConstructor
public class ResourceValidator {
    private SmartValidator validator;

    public void validate(Object objectToValidate) {
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(objectToValidate, objectToValidate.getClass().getSimpleName());
        validator.validate(objectToValidate, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
    }
}