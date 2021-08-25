package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.City;
import com.ronijr.algafoodapi.domain.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityQueryService {
    @Autowired
    private CityRepository cityRepository;

    public List<City> findAll() {
        return cityRepository.findAll();
    }

    public Optional<City> findById(Long id) {
        return cityRepository.findById(id);
    }

    public City findByIdOrElseThrow(Long id) throws EntityNotFoundException {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("City", id.toString()));
    }
}