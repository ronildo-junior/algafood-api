package com.ronijr.algafoodapi.domain.service.query;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.State;
import com.ronijr.algafoodapi.domain.repository.StateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StateQuery {
    private final StateRepository stateRepository;
    private final AppMessageSource messageSource;

    public List<State> findAll() {
        return stateRepository.findAll();
    }

    public Optional<State> findById(Long id) {
        return stateRepository.findById(id);
    }

    public State findByIdOrElseThrow(Long id) throws EntityNotFoundException {
        return stateRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("state.not.found", id)));
    }
}