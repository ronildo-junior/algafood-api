package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.State;
import com.ronijr.algafoodapi.domain.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateQueryService {
    @Autowired
    private StateRepository stateRepository;

    public List<State> findAll(){
        return stateRepository.list();
    }

    public State findById(Long id){
        try {
            return stateRepository.get(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(id);
        }
    }
}
