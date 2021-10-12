package com.ronijr.algafoodapi.config.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.ronijr.algafoodapi.config.security.SecurityConstants.*;

public @interface CheckSecurity {
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
}