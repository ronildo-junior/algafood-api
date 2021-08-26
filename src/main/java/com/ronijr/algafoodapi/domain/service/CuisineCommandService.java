package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipException;
import com.ronijr.algafoodapi.domain.exception.EntityRequiredPropertyEmptyException;
import com.ronijr.algafoodapi.domain.exception.EntityUniqueViolationException;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import com.ronijr.algafoodapi.domain.repository.CuisineRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CuisineCommandService {
    private final CuisineRepository cuisineRepository;
    private final AppMessageSource messageSource;

    public Cuisine create(Cuisine cuisine) throws EntityRequiredPropertyEmptyException, EntityUniqueViolationException {
        return update(cuisine);
    }

    public Cuisine update(Cuisine cuisine) throws EntityRequiredPropertyEmptyException, EntityUniqueViolationException {
        if (cuisine.getName() == null || cuisine.getName().trim().equals("")) {
            throw new EntityRequiredPropertyEmptyException(messageSource.getMessage("cuisine.name.not.empty"));
        }
        try {
            return cuisineRepository.save(cuisine);
        } catch (DataIntegrityViolationException e) {
            throw new EntityUniqueViolationException(
                    messageSource.getMessage("cuisine.name.unique", new Object[] { cuisine.getName() }));
        }
    }

    public void delete(Long id) throws EntityRelationshipException, EntityNotFoundException {
        try {
            Cuisine cuisine = findById(id);
            cuisineRepository.delete(cuisine);
        } catch (DataIntegrityViolationException e) {
            throw new EntityRelationshipException(
                    messageSource.getMessage("cuisine.relationship.found", new Object[] { id }));
        }
    }

    private Cuisine findById(Long id) throws EntityNotFoundException {
        return cuisineRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("cuisine.not.found", new Object[] { id })));
    }
}
