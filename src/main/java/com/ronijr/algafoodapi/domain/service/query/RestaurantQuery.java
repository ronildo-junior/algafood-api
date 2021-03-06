package com.ronijr.algafoodapi.domain.service.query;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.Product;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import com.ronijr.algafoodapi.domain.model.User;
import com.ronijr.algafoodapi.domain.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.ronijr.algafoodapi.infrastructure.repository.specification.RestaurantSpecification.withFreeDelivery;

@Service
@AllArgsConstructor
public class RestaurantQuery {
    private final RestaurantRepository restaurantRepository;
    private final AppMessageSource messageSource;

    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public Optional<Restaurant> findById(Long id) {
        return restaurantRepository.findById(id);
    }

    public Restaurant findByIdOrElseThrow(Long id) throws EntityNotFoundException {
        return findById(id).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("restaurant.not.found", id)));
    }

    public Restaurant findForUpdate(Long id) throws EntityNotFoundException {
        Restaurant current = findByIdOrElseThrow(id);
        if (current.getCuisine() != null) {
            restaurantRepository.detach(current.getCuisine());
        }
        if (current.getAddress().getCity() != null) {
            restaurantRepository.detach(current.getAddress().getCity());
        }
        return current;
    }

    public Optional<OffsetDateTime> getLastUpdateDate() {
        return restaurantRepository.getLastUpdateDate();
    }

    public List<Restaurant> customQuery(Map<String, Object> parameters){
        return restaurantRepository.findCriteriaTest(parameters);
    }

    public List<Restaurant> findAllWithFreeDelivery(){
        return restaurantRepository.findAll(withFreeDelivery());
    }

    public Restaurant findFirst() throws EntityNotFoundException {
        return restaurantRepository.findFirst().orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("resource.list.empty")));
    }

    public Set<Product> getProductList(Long restaurantId) {
        Restaurant restaurant = findByIdOrElseThrow(restaurantId);
        return restaurant.getProducts();
    }

    public Product getProduct(Long restaurantId, Long productId) {
        Restaurant restaurant = findByIdOrElseThrow(restaurantId);
        return restaurant.getProduct(productId).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("product.not.found", productId)));
    }

    public Set<User> getUsers(Long restaurantId) {
        Restaurant restaurant = findByIdOrElseThrow(restaurantId);
        return restaurant.getUsers();
    }

    public User getUser(Long restaurantId, Long userId) {
        Restaurant restaurant = findByIdOrElseThrow(restaurantId);
        return restaurant.getUser(userId).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("user.not.found", userId)));
    }

    public boolean userManageRestaurant(Long restaurantId, Long userId) {
        return restaurantRepository.existsManagerInRestaurant(userId, restaurantId);
    }
}