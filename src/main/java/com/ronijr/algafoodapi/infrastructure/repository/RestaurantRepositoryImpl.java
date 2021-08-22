package com.ronijr.algafoodapi.infrastructure.repository;

import com.ronijr.algafoodapi.domain.model.Restaurant;
import com.ronijr.algafoodapi.domain.repository.RestaurantRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class RestaurantRepositoryImpl implements RestaurantRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Restaurant> list() {
        return entityManager.createQuery("from Restaurant", Restaurant.class).getResultList();
    }

    @Override
    public Restaurant get(Long id) {
        return entityManager.find(Restaurant.class, id);
    }

    @Transactional
    @Override
    public Restaurant add(Restaurant restaurant) {
        return entityManager.merge(restaurant);
    }

    @Transactional
    @Override
    public void remove(Long id) {
        Restaurant restaurant = get(id);
        if (restaurant == null) {
            throw new EmptyResultDataAccessException(1);
        }
        entityManager.remove(restaurant);
    }
}
