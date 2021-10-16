package com.ronijr.algafoodapi.config.security;

import com.ronijr.algafoodapi.domain.service.query.OrderQuery;
import com.ronijr.algafoodapi.domain.service.query.RestaurantQuery;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AlgaSecurity {
    private final RestaurantQuery restaurantQuery;
    private final OrderQuery orderQuery;

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Long getUserId() {
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
        return getAuthentication().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(authorityName));
    }

    public boolean allowManageOrder(String orderCode) {
        return hasAuthority(SecurityConstants.Scope.ALLOW_WRITE) &&
                (hasAuthority(SecurityConstants.Order.ALLOW_EDIT) || userManageOrder(orderCode));
    }
}