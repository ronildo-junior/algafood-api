package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.City;
import com.ronijr.algafoodapi.domain.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityQueryService {
    @Autowired
    private CityRepository cityRepository;

    public List<City> findAll(){
        return cityRepository.list();
    }

    public City findById(Long id){
        try {
            return cityRepository.get(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(id);
        }
    }
}
