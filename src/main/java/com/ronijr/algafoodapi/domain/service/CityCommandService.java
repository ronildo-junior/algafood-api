package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRequiredPropertyEmptyException;
import com.ronijr.algafoodapi.domain.model.City;
import com.ronijr.algafoodapi.domain.model.State;
import com.ronijr.algafoodapi.domain.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CityCommandService {
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private StateQueryService stateQueryService;

    public City create(City city) {
        if (city.getState() == null || city.getState().getId() == null) {
            throw new EntityRequiredPropertyEmptyException("State with id is required");
        }
        Long stateId = city.getState().getId();
        State state = stateQueryService.findById(stateId);
        if (state == null) {
            throw new EntityNotFoundException(String.format("State with id %d not found", stateId));
        }
        city.setState(state);
        return cityRepository.add(city);
    }

    public City update(City city) {
        if (city.getState() == null || city.getState().getId() == null) {
            throw new EntityRequiredPropertyEmptyException("State with id is required");
        }
        Long stateId = city.getState().getId();
        State state = stateQueryService.findById(stateId);
        if (state == null) {
            throw new EntityNotFoundException(String.format("State with id %d not found", stateId));
        }
        try {
            city.setState(state);
            return cityRepository.add(city);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("City with id %d not found", city.getId()));
        }
    }

    public void delete(Long id) {
        try {
            cityRepository.remove(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("City with id %d not found", id));
        }
    }
}
