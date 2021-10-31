package com.ronijr.algafoodapi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface CustomJpaRepository<T, D> extends JpaRepository<T, D>, JpaSpecificationExecutor<T> {
    Optional<T> findFirst();
    T findByIdOrElseThrow(D id);
    void detach(Object entity);
}
