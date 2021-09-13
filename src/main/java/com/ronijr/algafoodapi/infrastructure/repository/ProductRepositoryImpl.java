package com.ronijr.algafoodapi.infrastructure.repository;

import com.ronijr.algafoodapi.domain.model.ProductPhoto;
import com.ronijr.algafoodapi.domain.repository.ProductPhotoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ProductRepositoryImpl implements ProductPhotoRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    @Override
    public ProductPhoto save(ProductPhoto productPhoto) {
        return entityManager.merge(productPhoto);
    }

    @Transactional
    @Override
    public void delete(ProductPhoto productPhoto) {
        entityManager.remove(productPhoto);
    }

    @Override
    public Optional<ProductPhoto> findPhotoByIdAndRestaurantId(Long productId, Long restaurantId) {
        TypedQuery<ProductPhoto> query = entityManager.createQuery(
                "select f " +
                        "from ProductPhoto f join f.product p " +
                        "where f.product.id = " + productId +
                        "  and p.restaurant.id = " + restaurantId, ProductPhoto.class);
        return query.getResultList().stream().findFirst();
    }
}