package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipException;
import com.ronijr.algafoodapi.domain.exception.EntityRequiredPropertyEmptyException;
import com.ronijr.algafoodapi.domain.model.State;
import com.ronijr.algafoodapi.domain.repository.StateRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StateCommandService {
    private StateRepository stateRepository;
    private final AppMessageSource messageSource;

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

    public void delete(Long id) throws EntityRelationshipException, EntityNotFoundException {
        try {
            State state = findById(id);
            stateRepository.delete(state);
        } catch (DataIntegrityViolationException e) {
            throw new EntityRelationshipException(
                    String.format("State with id %d can not be deleted.", id));
        }
    }

    private State findById(Long id) throws EntityNotFoundException {
        return stateRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("state.not.found")));
    }
}