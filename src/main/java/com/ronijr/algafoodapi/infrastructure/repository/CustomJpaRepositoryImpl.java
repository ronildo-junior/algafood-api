package com.ronijr.algafoodapi.infrastructure.repository;

import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.repository.CustomJpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.util.Optional;

public class CustomJpaRepositoryImpl<T, D> extends SimpleJpaRepository<T, D> implements CustomJpaRepository<T, D> {

    private final EntityManager entityManager;

    public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Optional<T> findFirst() {
        return entityManager.createQuery("from " + getDomainClass().getName(), getDomainClass()).
                setMaxResults(1).
                getResultList().
                stream().
                findFirst();
    }

    @Override
    public T findByIdOrElseThrow(D id) {
        return findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        getDomainClass().getName() + " with id " + id.toString() + " not found."));
    }

    @Override
    public void detach(Object entity) {
        entityManager.detach(entity);
    }
}
