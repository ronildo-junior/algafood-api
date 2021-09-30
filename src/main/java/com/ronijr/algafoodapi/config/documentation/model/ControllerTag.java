package com.ronijr.algafoodapi.config.documentation.model;

import com.ronijr.algafoodapi.api.v1.controller.*;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Getter
public enum ControllerTag {
    CITY( "Cities", "Manage Cities", CityController.class.getSimpleName()),
    CUISINE("Cuisines", "Manage Cuisines", CuisineController.class.getSimpleName()),
    ORDER("Orders", "Manage Orders",
            OrderController.class.getSimpleName(), OrderStatusController.class.getSimpleName()),
    PAYMENT_METHOD( "Payment Methods", "Manage Payment Methods", PaymentMethodController.class.getSimpleName()),
    PERMISSION("Permissions", "Manage Permissions", PermissionController.class.getSimpleName()),
    PRODUCT( "Products", "Manage Products",
            RestaurantProductController.class.getSimpleName(), RestaurantProductPhotoController.class.getSimpleName()),
    RESTAURANT( "Restaurants", "Manage Restaurants",
            RestaurantController.class.getSimpleName(), RestaurantPaymentMethodController.class.getSimpleName(),
            RestaurantUserController.class.getSimpleName()),
    ROOT_ENTRY_POINT("Root", "Discover API", RootEntryPointController.class.getSimpleName()),
    STATE( "States", "Manage States", StateController.class.getSimpleName()),
    STATISTIC( "Statistics", "Manage Statistics", StatisticsController.class.getSimpleName()),
    USER( "Users", "Manage Users",
            UserController.class.getSimpleName(), UserGroupUserController.class.getSimpleName()),
    USER_GROUP( "User Groups", "Manage User Groups",
            UserGroupController.class.getSimpleName(), UserGroupPermissionController.class.getSimpleName());

    private final Set<String> types = new HashSet<>();
    private final String title;
    private final String description;

    ControllerTag(String title, String description, String type, String... types) {
        this.types.add(type);
        if (types.length > 0) {
            this.types.addAll(Arrays.asList(types));
        }
        this.title = title;
        this.description = description;
    }

    public static Optional<ControllerTag> findByType(String type) {
        for (ControllerTag v : values()) {
            if (v.getTypes().contains(type)) {
                return Optional.of(v);
            }
        }
        return Optional.empty();
    }

    public Tag getTag() {
        Tag tag = new Tag();
        tag.setName(this.title);
        tag.setDescription(this.description);
        return tag;
    }
}