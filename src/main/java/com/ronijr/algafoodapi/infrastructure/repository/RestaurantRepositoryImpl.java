package com.ronijr.algafoodapi.infrastructure.repository;

import com.ronijr.algafoodapi.domain.model.Restaurant;
import com.ronijr.algafoodapi.domain.repository.RestaurantRepositoryQueries;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;

@Repository
public class RestaurantRepositoryImpl implements RestaurantRepositoryQueries {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Restaurant> findCriteriaTest(Map<String, Object> parameters) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Restaurant> query = builder.createQuery(Restaurant.class);
        Root<Restaurant> root = query.from(Restaurant.class);

        parameters.forEach((key, value) -> query.where(builder.equal(root.get(key), value)));

        return entityManager.createQuery(query).getResultList();
    }
}
