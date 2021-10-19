package com.ronijr.algafoodapi.config.security;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.ronijr.algafoodapi.config.security.SecurityConstants.*;

public @interface CheckSecurity {
    @interface Cities {
        @PreAuthorize(Scope.ALLOW_WRITE + AND + City.ALLOW_CREATE)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowCreate {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + City.ALLOW_EDIT)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowEdit {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + City.ALLOW_DELETE)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowDelete {}

        @PreAuthorize(City.ALLOW_QUERY)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowRead {}
    }

    @interface Cuisines {
        @PreAuthorize(Scope.ALLOW_WRITE + AND + Cuisine.ALLOW_CREATE)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowCreate {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + Cuisine.ALLOW_EDIT)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowEdit {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + Cuisine.ALLOW_DELETE)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowDelete {}

        @PreAuthorize(Cuisine.ALLOW_QUERY)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowRead {}
    }

    @interface Orders {
        @PreAuthorize(Scope.ALLOW_WRITE + AND + IS_AUTHENTICATED)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowCreate {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + Order.ALLOW_EDIT)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowEdit {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + Order.ALLOW_DELETE)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowDelete {}

        @PreAuthorize(Scope.ALLOW_READ + AND
                + "(" + Order.ALLOW_READ
                + OR + "@algaSecurity.userAuthenticatedEquals(#filter.customerId)"
                + OR + "@algaSecurity.manageRestaurant(#filter.restaurantId)" + ")")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface AllowList {}

        @PreAuthorize("@algaSecurity.allowManageOrder(#orderCode)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface AllowManage {}

        @PreAuthorize(Scope.ALLOW_READ + AND + IS_AUTHENTICATED)
        @PostAuthorize(Order.ALLOW_READ
                + OR + "@algaSecurity.getUserId() == returnObject.body.customer.id"
                + OR + "@algaSecurity.manageRestaurant(returnObject.body.restaurant.id)")
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowQuery {}
    }

    @interface PaymentMethods {
        @PreAuthorize(Scope.ALLOW_WRITE + AND + PaymentMethod.ALLOW_CREATE)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowCreate {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + PaymentMethod.ALLOW_EDIT)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowEdit {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + PaymentMethod.ALLOW_DELETE)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowDelete {}

        @PreAuthorize(PaymentMethod.ALLOW_QUERY)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowRead {}
    }

    @interface Products {
        @PreAuthorize(Scope.ALLOW_WRITE + AND +
                "(" + Product.ALLOW_CREATE + OR + Restaurant.MANAGE_RESTAURANT + ")")
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowCreate {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND +
                "(" + Product.ALLOW_EDIT + OR + Restaurant.MANAGE_RESTAURANT + ")")
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowEdit {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND  +
                "(" + Product.ALLOW_DELETE + OR + Restaurant.MANAGE_RESTAURANT + ")")
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowDelete {}

        @PreAuthorize(Product.ALLOW_QUERY)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowRead {}
    }

    @interface Restaurants {
        @PreAuthorize(Scope.ALLOW_WRITE + AND + Restaurant.ALLOW_CREATE)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowCreate {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + Restaurant.ALLOW_EDIT)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowEdit {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND +
                "(" + Restaurant.ALLOW_EDIT + OR + Restaurant.MANAGE_RESTAURANT + ")")
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowManage {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + Restaurant.ALLOW_DELETE)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowDelete {}

        @PreAuthorize(Restaurant.ALLOW_QUERY)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowRead {}
    }

    @interface States {
        @PreAuthorize(Scope.ALLOW_WRITE + AND + State.ALLOW_CREATE)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowCreate {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + State.ALLOW_EDIT)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowEdit {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + State.ALLOW_DELETE)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowDelete {}

        @PreAuthorize(State.ALLOW_QUERY)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowRead {}
    }

    @interface Statistics {
        @PreAuthorize("@algaSecurity.allowQueryDailySales()")
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowIssueDailySales {}
    }

    @interface Users {
        @PreAuthorize(Scope.ALLOW_WRITE + AND + "(" + User.ALLOW_EDIT + OR + User.SELF + ")")
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowEdit {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + User.SELF)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowEditPassword {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + User.ALLOW_DELETE)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowDelete {}

        @PreAuthorize(User.ALLOW_QUERY)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowRead {}
    }

    @interface UserGroups {
        @PreAuthorize(Scope.ALLOW_WRITE + AND + UserGroup.ALLOW_CREATE)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowCreate {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + UserGroup.ALLOW_EDIT)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowEdit {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + UserGroup.ALLOW_DELETE)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowDelete {}

        @PreAuthorize("@algaSecurity.allowEditUserGroup")
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowRead {}
    }
}