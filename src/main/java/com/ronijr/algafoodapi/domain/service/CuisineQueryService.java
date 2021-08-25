package com.ronijr.algafoodapi.domain.service;

import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import com.ronijr.algafoodapi.domain.repository.CuisineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<Cuisine> findById(Long id) {
        return cuisineRepository.findById(id);
    }

    public Cuisine findByIdOrElseThrow(Long id) throws EntityNotFoundException {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("Cuisine", id.toString()));
    }
}