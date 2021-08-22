package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.exception.CuisineNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipException;
import com.ronijr.algafoodapi.domain.exception.EntityRequiredPropertyEmptyException;
import com.ronijr.algafoodapi.domain.exception.EntityUniqueViolationException;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import com.ronijr.algafoodapi.domain.repository.CuisineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CuisineCommandService {
    @Autowired
    private CuisineRepository cuisineRepository;

    public Cuisine create(Cuisine cuisine) throws EntityRequiredPropertyEmptyException, EntityUniqueViolationException {
        return update(cuisine);
    }

    public Cuisine update(Cuisine cuisine) throws EntityRequiredPropertyEmptyException, EntityUniqueViolationException {
        if (cuisine.getName() == null || cuisine.getName().trim().equals("")) {
            throw new EntityRequiredPropertyEmptyException("Cuisine name is required.");
        }
        try {
            return cuisineRepository.save(cuisine);
        } catch (DataIntegrityViolationException e) {
            throw new EntityUniqueViolationException(
                    String.format("Cuisine with name %s already registered.", cuisine.getName()));
        }
    }

    public void delete(Long id) throws EntityRelationshipException, CuisineNotFoundException {
        try {
            cuisineRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntityRelationshipException(String.format("Cuisine with id %d can not be deleted.", id));
        } catch (EmptyResultDataAccessException e) {
            throw new CuisineNotFoundException(id);
        }
    }
}
