package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.City;
import com.ronijr.algafoodapi.domain.repository.CityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CityQuery {
    private final CityRepository cityRepository;
    private final AppMessageSource messageSource;

    public List<City> findAll() {
        return cityRepository.findAll();
    }

    public Optional<City> findById(Long id) {
        return cityRepository.findById(id);
    }

    public City findByIdOrElseThrow(Long id) throws EntityNotFoundException {
        return findById(id).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("city.not.found", id)));
    }
}