package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.State;
import com.ronijr.algafoodapi.domain.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StateQueryService {
    @Autowired
    private StateRepository stateRepository;

    public List<State> findAll() {
        return stateRepository.findAll();
    }

    public Optional<State> findById(Long id) {
        return stateRepository.findById(id);
    }

    public State findByIdOrElseThrow(Long id) throws EntityNotFoundException {
        return stateRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("State", id.toString()));
    }
}