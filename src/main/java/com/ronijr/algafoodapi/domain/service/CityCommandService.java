package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRequiredPropertyEmptyException;
import com.ronijr.algafoodapi.domain.model.City;
import com.ronijr.algafoodapi.domain.model.State;
import com.ronijr.algafoodapi.domain.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CityCommandService {
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private StateQueryService stateQueryService;

    public City create(City city) throws EntityRequiredPropertyEmptyException, EntityRelationshipNotFoundException {
        return update(city);
    }

    public City update(City city) throws EntityRequiredPropertyEmptyException, EntityRelationshipNotFoundException {
        Long stateId = Optional.ofNullable(city.getState()).
                map(State::getId).
                orElseThrow(() ->
                        new EntityRequiredPropertyEmptyException("State with id is required."));
        State state = stateQueryService.findById(stateId).
                orElseThrow(() ->
                        new EntityRelationshipNotFoundException(
                                String.format("State with id %d not found.", stateId)));
        city.setState(state);
        return cityRepository.save(city);
    }

    public void delete(Long id) throws EntityNotFoundException, EntityRelationshipException {
        try{
            City city = findById(id);
            cityRepository.delete(city);
        } catch (DataIntegrityViolationException e) {
            throw new EntityRelationshipException(String.format("City with id %d can not be deleted.", id));
        }
    }

    private City findById(Long id) throws EntityNotFoundException {
        return cityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("City with id %d not found.", id)));
    }
}