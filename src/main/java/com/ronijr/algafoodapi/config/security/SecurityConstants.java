package com.ronijr.algafoodapi.config.security;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityConstants {
    public static final String AND = " and ";
    public static final String OR = " or ";
    public static final String HAS_AUTHORITY = "hasAuthority";
    public static final String IS_AUTHENTICATED = "isAuthenticated()";

    @UtilityClass
    public static class Scope {
        public static final String READ = "SCOPE_READ";
        public static final String WRITE = "SCOPE_WRITE";
        public static final String ALLOW_READ = HAS_AUTHORITY + "('" + READ + "')";
        public static final String ALLOW_WRITE = HAS_AUTHORITY + "('" + WRITE + "')";
    }

    @UtilityClass
    public static class City {
        public static final String CREATE = "CREATE_CITIES";
        public static final String EDIT = "EDIT_CITIES";
        public static final String DELETE = "DELETE_CITIES";
        public static final String ALLOW_CREATE = HAS_AUTHORITY + "('" + CREATE + "')";
        public static final String ALLOW_EDIT = HAS_AUTHORITY + "('" + EDIT + "')";
        public static final String ALLOW_DELETE = HAS_AUTHORITY + "('" + DELETE + "')";
    }

    @UtilityClass
    public static class Cuisine {
        public static final String CREATE = "CREATE_CUISINES";
        public static final String EDIT = "EDIT_CUISINES";
        public static final String DELETE = "DELETE_CUISINES";
        public static final String ALLOW_CREATE = HAS_AUTHORITY + "('" + CREATE + "')";
        public static final String ALLOW_EDIT = HAS_AUTHORITY + "('" + EDIT + "')";
        public static final String ALLOW_DELETE = HAS_AUTHORITY + "('" + DELETE + "')";
    }

    @UtilityClass
    public static class Order {
        public static final String CREATE = "CREATE_ORDERS";
        public static final String EDIT = "EDIT_ORDERS";
        public static final String DELETE = "DELETE_ORDERS";
        public static final String READ = "READ_ORDERS";
        public static final String ALLOW_CREATE = HAS_AUTHORITY + "('" + CREATE + "')";
        public static final String ALLOW_EDIT = HAS_AUTHORITY + "('" + EDIT + "')";
        public static final String ALLOW_DELETE = HAS_AUTHORITY + "('" + DELETE + "')";
        public static final String ALLOW_READ = HAS_AUTHORITY + "('" + READ + "')";
    }

    @UtilityClass
    public static class PaymentMethod {
        public static final String CREATE = "CREATE_PAYMENT_METHODS";
        public static final String EDIT = "EDIT_PAYMENT_METHODS";
        public static final String DELETE = "DELETE_PAYMENT_METHODS";
        public static final String ALLOW_CREATE = HAS_AUTHORITY + "('" + CREATE + "')";
        public static final String ALLOW_EDIT = HAS_AUTHORITY + "('" + EDIT + "')";
        public static final String ALLOW_DELETE = HAS_AUTHORITY + "('" + DELETE + "')";
    }

    @UtilityClass
    public static class Permission {
        public static final String CREATE = "CREATE_PERMISSIONS";
        public static final String EDIT = "EDIT_PERMISSIONS";
        public static final String DELETE = "DELETE_PERMISSIONS";
        public static final String READ = "READ_PERMISSIONS";
        public static final String ALLOW_CREATE = HAS_AUTHORITY + "('" + CREATE + "')";
        public static final String ALLOW_EDIT = HAS_AUTHORITY + "('" + EDIT + "')";
        public static final String ALLOW_DELETE = HAS_AUTHORITY + "('" + DELETE + "')";
        public static final String ALLOW_READ = HAS_AUTHORITY + "('" + READ + "')";
    }

    @UtilityClass
    public static class Product {
        public static final String CREATE = "CREATE_PRODUCTS";
        public static final String EDIT = "EDIT_PRODUCTS";
        public static final String DELETE = "DELETE_PRODUCTS";
        public static final String ALLOW_CREATE = HAS_AUTHORITY + "('" + CREATE + "')";
        public static final String ALLOW_EDIT = HAS_AUTHORITY + "('" + EDIT + "')";
        public static final String ALLOW_DELETE = HAS_AUTHORITY + "('" + DELETE + "')";
    }

    @UtilityClass
    public static class Restaurant {
        public static final String CREATE = "CREATE_RESTAURANTS";
        public static final String EDIT = "EDIT_RESTAURANTS";
        public static final String DELETE = "DELETE_RESTAURANTS";
        public static final String ALLOW_CREATE = HAS_AUTHORITY + "('" + CREATE + "')";
        public static final String ALLOW_EDIT = HAS_AUTHORITY + "('" + EDIT + "')";
        public static final String ALLOW_DELETE = HAS_AUTHORITY + "('" + DELETE + "')";
        public static final String MANAGE_RESTAURANT = "@algaSecurity.manageRestaurant(#restaurantId)";
    }

    @UtilityClass
    public static class State {
        public static final String CREATE = "CREATE_STATES";
        public static final String EDIT = "EDIT_STATES";
        public static final String DELETE = "DELETE_STATES";
        public static final String ALLOW_CREATE = HAS_AUTHORITY + "('" + CREATE + "')";
        public static final String ALLOW_EDIT = HAS_AUTHORITY + "('" + EDIT + "')";
        public static final String ALLOW_DELETE = HAS_AUTHORITY + "('" + DELETE + "')";
    }

    @UtilityClass
    public static class User {
        public static final String CREATE = "CREATE_USERS";
        public static final String EDIT = "EDIT_USERS";
        public static final String DELETE = "DELETE_USERS";
        public static final String READ = "READ_USERS";
        public static final String ALLOW_CREATE = HAS_AUTHORITY + "('" + CREATE + "')";
        public static final String ALLOW_EDIT = HAS_AUTHORITY + "('" + EDIT + "')";
        public static final String ALLOW_DELETE = HAS_AUTHORITY + "('" + DELETE + "')";
        public static final String ALLOW_READ = HAS_AUTHORITY + "('" + READ + "')";
        public static final String SELF = "@algaSecurity.getUserId() == #userId";
    }

    @UtilityClass
    public static class UserGroup {
        public static final String CREATE = "CREATE_USER_GROUPS";
        public static final String EDIT = "EDIT_USER_GROUPS";
        public static final String DELETE = "DELETE_USER_GROUPS";
        public static final String READ = "READ_USER_GROUPS";
        public static final String ALLOW_CREATE = HAS_AUTHORITY + "('" + CREATE + "')";
        public static final String ALLOW_EDIT = HAS_AUTHORITY + "('" + EDIT + "')";
        public static final String ALLOW_DELETE = HAS_AUTHORITY + "('" + DELETE + "')";
        public static final String ALLOW_READ = HAS_AUTHORITY + "('" + READ + "')";
    }
}