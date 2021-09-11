package com.ronijr.algafoodapi.domain.service.command;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipException;
import com.ronijr.algafoodapi.domain.exception.ValidationException;
import com.ronijr.algafoodapi.domain.model.State;
import com.ronijr.algafoodapi.domain.repository.StateRepository;
import com.ronijr.algafoodapi.domain.validation.ResourceValidator;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class StateCommand {
    private final StateRepository stateRepository;
    private final AppMessageSource messageSource;
    private final ResourceValidator validator;

    public State create(State state) throws ValidationException {
        return update(state);
    }

    public State update(State state) throws ValidationException {
        validator.validate(state);
        return stateRepository.save(state);
    }

    public void delete(Long id) throws EntityRelationshipException, EntityNotFoundException {
        try {
            State state = findById(id);
            stateRepository.delete(state);
            stateRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntityRelationshipException(messageSource.getMessage("state.relationship.found", id));
        }
    }

    private State findById(Long id) throws EntityNotFoundException {
        return stateRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage("state.not.found", id)));
    }
}