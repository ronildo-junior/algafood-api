package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
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

    private Cuisine add(Cuisine cuisine){
        if (cuisine.getName() == null) {
            throw new EntityRequiredPropertyEmptyException("Cuisine name is required");
        }
        try {
            return cuisineRepository.add(cuisine);
        } catch (DataIntegrityViolationException e) {
            throw new EntityUniqueViolationException(
                    String.format("Cuisine with name %s already registered", cuisine.getName()));
        }
    }

    public Cuisine create(Cuisine cuisine) {
        return add(cuisine);
    }

    public Cuisine update(Cuisine cuisine) {
        return add(cuisine);
    }

    public void delete(Long id) {
        try {
            cuisineRepository.remove(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntityRelationshipException(String.format("Cuisine with id %d can not be deleted.", id));
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(
                    String.format("Cuisine with id %d not found", id));
        }
    }
}
