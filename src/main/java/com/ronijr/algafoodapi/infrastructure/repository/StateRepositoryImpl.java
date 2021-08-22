package com.ronijr.algafoodapi.infrastructure.repository;

import com.ronijr.algafoodapi.domain.model.State;
import com.ronijr.algafoodapi.domain.repository.StateRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class StateRepositoryImpl implements StateRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<State> list() {
        return entityManager.createQuery("from State", State.class).getResultList();
    }
    @Override
    public State get(Long id) {
        return entityManager.find(State.class, id);
    }
    @Transactional
    @Override
    public State add(State state) {
        return entityManager.merge(state);

    }
    @Transactional
    @Override
    public void remove(Long id) {
        State state = get(id);
        if (state == null) {
            throw new EmptyResultDataAccessException(1);
        }
        entityManager.remove(state);
    }
}
