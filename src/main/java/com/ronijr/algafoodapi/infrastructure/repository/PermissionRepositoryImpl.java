package com.ronijr.algafoodapi.infrastructure.repository;

import com.ronijr.algafoodapi.domain.model.Permission;
import com.ronijr.algafoodapi.domain.repository.PermissionRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class PermissionRepositoryImpl implements PermissionRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Permission> list() {
        return entityManager.createQuery("from Permission", Permission.class).getResultList();
    }

    @Override
    public Permission get(Long id) {
        return entityManager.find(Permission.class, id);
    }

    @Transactional
    @Override
    public Permission add(Permission permission) {
        return entityManager.merge(permission);
    }

    @Transactional
    @Override
    public void remove(Long id) {
        Permission permission = get(id);
        if (permission == null) {
            throw new EmptyResultDataAccessException(1);
        }
        entityManager.remove(permission);
    }
}
