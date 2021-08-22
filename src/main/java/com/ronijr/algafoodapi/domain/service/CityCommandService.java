package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRequiredPropertyEmptyException;
import com.ronijr.algafoodapi.domain.exception.StateNotFoundException;
import com.ronijr.algafoodapi.domain.model.City;
import com.ronijr.algafoodapi.domain.model.State;
import com.ronijr.algafoodapi.domain.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CityCommandService {
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private StateQueryService stateQueryService;

    public City create(City city) throws EntityRequiredPropertyEmptyException, StateNotFoundException {
        return update(city);
    }

    public City update(City city) throws EntityRequiredPropertyEmptyException, StateNotFoundException {
        Long stateId = Optional.ofNullable(city.getState()).
                map(State::getId).
                orElseThrow(() ->
                        new EntityRequiredPropertyEmptyException("State with id is required."));
        State state = stateQueryService.findById(stateId);
        city.setState(state);
        return cityRepository.save(city);
    }

    public void delete(Long id) throws EntityNotFoundException {
        try {
            cityRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("City with id %d not found", id));
        }
    }
}
