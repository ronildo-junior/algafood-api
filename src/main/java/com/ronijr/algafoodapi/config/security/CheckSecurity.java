package com.ronijr.algafoodapi.config.security;

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
        @PreAuthorize(Scope.ALLOW_WRITE + AND + Order.ALLOW_CREATE)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowCreate {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + Order.ALLOW_EDIT)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowEdit {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + Order.ALLOW_DELETE)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowDelete {}
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
                "(" + Product.ALLOW_CREATE + OR + "@algaSecurity.manageRestaurant(#restaurantId)" + ")")
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowCreate {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND +
                "(" + Product.ALLOW_EDIT + OR + "@algaSecurity.manageRestaurant(#restaurantId)" + ")")
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowEdit {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND  +
                "(" + Product.ALLOW_DELETE + OR + "@algaSecurity.manageRestaurant(#restaurantId)" + ")")
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
                "(" + Restaurant.ALLOW_EDIT + OR + "@algaSecurity.manageRestaurant(#restaurantId)" + ")")
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowManage {}

        @PreAuthorize(Scope.ALLOW_WRITE + AND + Restaurant.ALLOW_DELETE)
        @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
        @interface AllowDelete {}
    }
}