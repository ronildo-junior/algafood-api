package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.exception.EntityRelationshipException;
import com.ronijr.algafoodapi.domain.exception.EntityRequiredPropertyEmptyException;
import com.ronijr.algafoodapi.domain.exception.StateNotFoundException;
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

    public State create(State state) throws EntityRequiredPropertyEmptyException {
        return update(state);
    }

    public State update(State state) throws EntityRequiredPropertyEmptyException {
        if (state.getName() == null || state.getName().trim().equals("") ||
                state.getAbbreviation() == null || state.getAbbreviation().trim().equals("")) {
            throw new EntityRequiredPropertyEmptyException("State name and abbreviation are required.");
        }
        return stateRepository.save(state);
    }

    public void delete(Long id) throws EntityRelationshipException, StateNotFoundException {
        try {
            stateRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntityRelationshipException(
                    String.format("State with id %d can not be deleted.", id));
        } catch (EmptyResultDataAccessException e) {
            throw new StateNotFoundException(id);
        }
    }
}
