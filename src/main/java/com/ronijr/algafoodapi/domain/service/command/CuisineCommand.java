package com.ronijr.algafoodapi.domain.service.command;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipException;
import com.ronijr.algafoodapi.domain.exception.EntityUniqueViolationException;
import com.ronijr.algafoodapi.domain.exception.ValidationException;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import com.ronijr.algafoodapi.domain.repository.CuisineRepository;
import com.ronijr.algafoodapi.domain.validation.ResourceValidator;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class CuisineCommand {
    private final CuisineRepository cuisineRepository;
    private final AppMessageSource messageSource;
    private final ResourceValidator validator;

    public Cuisine create(Cuisine cuisine) throws ValidationException, EntityUniqueViolationException {
        return update(cuisine);
    }

    public Cuisine update(Cuisine cuisine) throws ValidationException, EntityUniqueViolationException {
        validator.validate(cuisine);
        try {
            return cuisineRepository.saveAndFlush(cuisine);
        } catch (DataIntegrityViolationException e) {
            throw new EntityUniqueViolationException(messageSource.getMessage("cuisine.name.unique", cuisine.getName()));
        }
    }

    public void delete(Long id) throws EntityRelationshipException, EntityNotFoundException {
        try {
            Cuisine cuisine = findById(id);
            cuisineRepository.delete(cuisine);
            cuisineRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntityRelationshipException(
                    messageSource.getMessage("cuisine.relationship.found", id));
        }
    }

    private Cuisine findById(Long id) throws EntityNotFoundException {
        return cuisineRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("cuisine.not.found", id)));
    }
}
