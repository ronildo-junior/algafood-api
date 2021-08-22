package com.ronijr.algafoodapi.infrastructure.repository;

import com.ronijr.algafoodapi.domain.model.City;
import com.ronijr.algafoodapi.domain.repository.CityRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class CityRepositoryImpl implements CityRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<City> list() {
        return entityManager.createQuery("from City", City.class).getResultList();
    }

    @Override
    public City get(Long id) {
        return  entityManager.find(City.class, id);
    }

    @Transactional
    @Override
    public City add(City city) {
        return entityManager.merge(city);
    }

    @Transactional
    @Override
    public void remove(Long id) {
        City city = get(id);
        if (city == null) {
            throw new EmptyResultDataAccessException(1);
        }
        entityManager.remove(city);
    }
}
