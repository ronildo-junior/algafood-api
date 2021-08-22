package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import com.ronijr.algafoodapi.domain.repository.CuisineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuisineQueryService {
    @Autowired
    private CuisineRepository cuisineRepository;

    public List<Cuisine> findAll(){
        return cuisineRepository.list();
    }

    public List<Cuisine> findByName(String name){
        return cuisineRepository.filterByName(name);
    }

    public Cuisine findById(Long id){
        try {
            return cuisineRepository.get(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(id);
        }
    }
}
