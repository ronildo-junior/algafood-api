package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipException;
import com.ronijr.algafoodapi.domain.exception.EntityRequiredPropertyEmptyException;
import com.ronijr.algafoodapi.domain.model.State;
import com.ronijr.algafoodapi.domain.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class StateCommandService {
    @Autowired
    private StateRepository stateRepository;

    public State create(State state) {
        if (state.getName() == null || state.getAbbreviation() == null) {
            throw new EntityRequiredPropertyEmptyException("State name and abbreviation are required");
        }
        return stateRepository.add(state);
    }

    public State update(State state) {
        if (state.getName() == null || state.getAbbreviation() == null) {
            throw new EntityRequiredPropertyEmptyException("State name and abbreviation are required");
        }
        try {
            return stateRepository.add(state);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("State with id %d not found", state.getId()));
        }
    }

    public void delete(Long id) {
        try {
            stateRepository.remove(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntityRelationshipException(
                    String.format("State with id %d can not be deleted.", id));
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("State with id %d not found", id));
        }
    }
}
