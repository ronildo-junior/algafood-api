package com.ronijr.algafoodapi.config.security;

import com.ronijr.algafoodapi.domain.service.query.OrderQuery;
import com.ronijr.algafoodapi.domain.service.query.RestaurantQuery;
import com.ronijr.algafoodapi.infrastructure.EnvironmentConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.config.security.SecurityConstants.*;

@Component
@RequiredArgsConstructor
public class AlgaSecurity {
    private final RestaurantQuery restaurantQuery;
    private final OrderQuery orderQuery;
    @Value("${spring.profiles.active:unknown}")
    private String activeProfile;

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Long getUserId() {
        if (currentProfileSkipSecurity()) {
            return 1L;
        }
        Jwt jwt = (Jwt) getAuthentication().getPrincipal();
        return jwt.getClaim("user_id");
    }

    public boolean manageRestaurant(Long restaurantId) {
        if (restaurantId == null) {
            return false;
        }
        return restaurantQuery.userManageRestaurant(restaurantId, getUserId());
    }

    public boolean userManageOrder(String code) {
        if (code == null) {
            return false;
        }
        return orderQuery.userManageOrder(getUserId(), code);
    }

    public boolean userAuthenticatedEquals(Long userId) {
        return getUserId() != null && userId != null && getUserId().equals(userId);
    }

    public boolean hasAuthority(String authorityName) {
        return currentProfileSkipSecurity() || getAuthentication().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(authorityName));
    }

    public boolean allowManageOrder(String orderCode) {
        return hasScopeWrite() && (hasAuthority(Order.EDIT) || userManageOrder(orderCode));
    }

    public boolean isAuthenticated() {
        return currentProfileSkipSecurity() || getAuthentication().isAuthenticated();
    }

    public boolean hasScopeWrite() {
        return currentProfileSkipSecurity() || hasAuthority(Scope.WRITE);
    }

    public boolean hasScopeRead() {
        return currentProfileSkipSecurity() || hasAuthority(Scope.READ);
    }

    public boolean allowEditRestaurant() {
        return hasScopeWrite() && hasAuthority(Restaurant.EDIT);
    }

    public boolean allowManageRestaurant(Long restaurantId) {
        return hasScopeWrite() && (hasAuthority(Restaurant.EDIT) || manageRestaurant(restaurantId));
    }

    public boolean allowQueryRestaurants() {
        return hasScopeRead() && isAuthenticated();
    }

    public boolean allowQueryProducts() {
        return hasScopeRead() && isAuthenticated();
    }

    public boolean allowQueryUsers() {
        return hasScopeRead() && hasAuthority(User.READ);
    }

    public boolean allowQueryUserGroupPermissions() {
        return hasScopeRead() && hasAuthority(UserGroup.READ);
    }

    public boolean allowEditUserGroup() {
        return hasScopeWrite() && hasAuthority(UserGroup.EDIT);
    }

    public boolean allowQueryOrders() {
        return hasScopeRead() && hasAuthority(Order.READ);
    }

    public boolean allowQueryOrders(Long customerId, Long restaurantId) {
        return hasScopeRead() && (hasAuthority(Order.READ)
                || userAuthenticatedEquals(customerId) || manageRestaurant(restaurantId));
    }

    public boolean allowQueryPaymentMethods() {
        return isAuthenticated() && hasScopeRead();
    }

    public boolean allowQueryCities() {
        return isAuthenticated() && hasScopeRead();
    }

    public boolean allowQueryStates() {
        return isAuthenticated() && hasScopeRead();
    }

    public boolean allowQueryCuisines() {
        return isAuthenticated() && hasScopeRead();
    }

    public boolean allowQueryDailySales() {
        return hasScopeRead() && hasAuthority(Statistics.READ_DAILY_SALES);
    }

    private boolean currentProfileSkipSecurity() {
        return !activeProfile.equals(EnvironmentConstants.HOMOLOGATION) &&
                !activeProfile.equals(EnvironmentConstants.PRODUCTION);
    }
}