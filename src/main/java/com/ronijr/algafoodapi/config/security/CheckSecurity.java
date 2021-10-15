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

        @PreAuthorize(Scope.ALLOW_READ + AND + IS_AUTHENTICATED)
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
                + OR + "@algaSecurity.getUserId() == #filter.customerId"
                + OR + "@algaSecurity.manageRestaurant(#filter.restaurantId)" + ")")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface AllowList {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND +
                "(" + Order.ALLOW_EDIT
                + OR + "@algaSecurity.userManageOrder(#orderCode))")
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

        @PreAuthorize(Scope.ALLOW_READ + AND + IS_AUTHENTICATED)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowRead {}
    }

    @interface Permissions {
        @PreAuthorize(Scope.ALLOW_WRITE + AND + Permission.ALLOW_CREATE)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowCreate {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + Permission.ALLOW_EDIT)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowEdit {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + Permission.ALLOW_DELETE)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowDelete {}
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

        @PreAuthorize(Scope.ALLOW_READ + AND + IS_AUTHENTICATED)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowRead {}
    }
}