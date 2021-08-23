package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.exception.CuisineNotFoundException;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import com.ronijr.algafoodapi.domain.repository.CuisineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuisineQueryService {
    @Autowired
    private CuisineRepository cuisineRepository;

    public List<Cuisine> findAll() {
        return cuisineRepository.findAll();
    }

    public List<Cuisine> findByName(String name) {
        return cuisineRepository.findAllByNameContaining(name);
    }

    public Cuisine findById(Long id) throws CuisineNotFoundException {
        return cuisineRepository.findById(id).orElseThrow(() -> new CuisineNotFoundException(id));
    }
}
