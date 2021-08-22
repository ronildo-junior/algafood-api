package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.exception.StateNotFoundException;
import com.ronijr.algafoodapi.domain.model.State;
import com.ronijr.algafoodapi.domain.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateQueryService {
    @Autowired
    private StateRepository stateRepository;

    public List<State> findAll() {
        return stateRepository.findAll();
    }

    public State findById(Long id) throws StateNotFoundException {
        return stateRepository.findById(id).orElseThrow(() -> new StateNotFoundException(id));
    }
}
