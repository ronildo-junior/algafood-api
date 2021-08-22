package com.ronijr.algafoodapi.infrastructure.repository;

import com.ronijr.algafoodapi.domain.model.PaymentMethod;
import com.ronijr.algafoodapi.domain.repository.PaymentMethodRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class PaymentMethodRepositoryImpl implements PaymentMethodRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PaymentMethod> list(){
        return entityManager.createQuery("from PaymentMethod", PaymentMethod.class).getResultList();
    }

    @Override
    public PaymentMethod get(Long id){
        return entityManager.find(PaymentMethod.class, id);
    }

    @Transactional
    @Override
    public PaymentMethod add(PaymentMethod paymentMethod){
        return entityManager.merge(paymentMethod);
    }

    @Transactional
    @Override
    public void remove(Long id){
        PaymentMethod paymentMethod = get(id);
        if (paymentMethod == null) {
            throw new EmptyResultDataAccessException(1);
        }
        entityManager.remove(paymentMethod);
    }
}
