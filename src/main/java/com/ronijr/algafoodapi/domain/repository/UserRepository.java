package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CustomJpaRepository<User, Long> {
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.name = :name and (:id is null or u.id <> :id)")
    boolean existsByNameAndIdNotEqual(String name, Long id);
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email and (:id is null or u.id <> :id)")
    boolean existsByEmailAndIdNotEqual(String email, Long id);
    Optional<User> findByEmail(String email);
}