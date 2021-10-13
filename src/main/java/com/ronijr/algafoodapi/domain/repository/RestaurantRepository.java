package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface RestaurantRepository
        extends CustomJpaRepository<Restaurant, Long>, RestaurantRepositoryQueries {
    @Override
    List<Restaurant> findCriteriaTest(Map<String, Object> parameters);
    @Query("from Restaurant r join fetch r.cuisine " +
            "left join fetch r.address.city " +
            "left join fetch r.address.city.state")
    List<Restaurant> findAll();
    @Query("select max(updatedAt) from Restaurant")
    Optional<OffsetDateTime> getLastUpdateDate();
    @Query("select count(r) > 0 from Restaurant r join r.users u where r.id = :restaurantId and u.id = :userId")
    boolean existsManagerInRestaurant(Long userId, Long restaurantId);
}