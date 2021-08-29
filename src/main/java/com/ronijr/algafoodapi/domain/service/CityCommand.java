package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipNotFoundException;
import com.ronijr.algafoodapi.domain.exception.ValidationException;
import com.ronijr.algafoodapi.domain.model.City;
import com.ronijr.algafoodapi.domain.model.State;
import com.ronijr.algafoodapi.domain.repository.CityRepository;
import com.ronijr.algafoodapi.domain.validation.ResourceValidator;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class CityCommand {
    private final CityRepository cityRepository;
    private final StateQuery stateQueryService;
    private final AppMessageSource messageSource;
    private final ResourceValidator validator;

    public City create(City city) throws ValidationException, EntityRelationshipNotFoundException {
        return update(city);
    }

    public City update(City city) throws ValidationException, EntityRelationshipNotFoundException {
        validator.validate(city);
        Long stateId = city.getState().getId();
        State state = stateQueryService.findById(stateId).
                orElseThrow(() -> new EntityRelationshipNotFoundException(
                        messageSource.getMessage("state.not.found", stateId)));
        city.setState(state);
        return cityRepository.save(city);
    }

    public void delete(Long id) throws EntityNotFoundException, EntityRelationshipException {
        try{
            City city = findById(id);
            cityRepository.delete(city);
        } catch (DataIntegrityViolationException e) {
            throw new EntityRelationshipException(
                    messageSource.getMessage("city.relationship.found", id));
        }
    }

    private City findById(Long id) throws EntityNotFoundException {
        return cityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("city.not.found", id)));
    }
}