package com.ronijr.algafoodapi.infrastructure.repository;

import com.ronijr.algafoodapi.domain.model.Cuisine;
import com.ronijr.algafoodapi.domain.repository.CuisineRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class CuisineRepositoryImpl implements CuisineRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Cuisine> list(){
        return entityManager.createQuery("from Cuisine", Cuisine.class).getResultList();
    }

    @Override
    public List<Cuisine> filterByName(String name) {
        return entityManager.createQuery("from Cuisine where name like :name", Cuisine.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }

    @Override
    public Cuisine get(Long id){
        return entityManager.find(Cuisine.class, id);
    }

    @Transactional
    @Override
    public Cuisine add(Cuisine cuisine){
        return entityManager.merge(cuisine);
    }

    @Transactional
    @Override
    public void remove(Long id){
        Cuisine cuisine = get(id);
        if (cuisine == null) {
            throw new EmptyResultDataAccessException(1);
        }
        entityManager.remove(cuisine);
    }
}
