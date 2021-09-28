package com.ronijr.algafoodapi.api.hateoas;

import com.ronijr.algafoodapi.api.controller.*;
import lombok.experimental.UtilityClass;
import org.springframework.hateoas.*;
import org.springframework.hateoas.TemplateVariable.VariableType;
import org.springframework.http.HttpMethod;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@UtilityClass
public class AlgaLinks {
    public static final TemplateVariables PAGE_VARIABLES = new TemplateVariables(
            new TemplateVariable("page", VariableType.REQUEST_PARAM),
            new TemplateVariable("size", VariableType.REQUEST_PARAM),
            new TemplateVariable("sort", VariableType.REQUEST_PARAM));
    public static final TemplateVariables PROJECTION_VARIABLES = new TemplateVariables(
            new TemplateVariable("projection", VariableType.REQUEST_PARAM));

    public Link linkToCity(Long cityId, String rel) {
        return linkTo(methodOn(CityController.class).get(cityId)).withRel(rel);
    }

    public Link linkToCity(Long cityId) {
        return linkToCity(cityId, IanaLinkRelations.SELF.value());
    }

    public Link linkToCities(String rel) {
        return linkTo(CityController.class).withRel(rel);
    }

    public Link linkToCities() {
        return linkToCities(IanaLinkRelations.SELF.value());
    }

    public Link linkToCuisine(Long cuisineId, String rel) {
        return linkTo(methodOn(CuisineController.class).get(cuisineId)).withRel(rel);
    }

    public Link linkToCuisine(Long cuisineId) {
        return linkToCuisine(cuisineId, IanaLinkRelations.SELF.value());
    }

    public Link linkToCuisines(String rel) {
        return linkTo(CuisineController.class).withRel(rel);
    }

    public Link linkToCuisines() {
        return linkToCuisines(IanaLinkRelations.SELF.value());
    }

    public Link linkToManagersRestaurant(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantUserController.class).list(restaurantId)).withRel(rel);
    }

    public Link linkToManagersRestaurant(Long restaurantId) {
        return linkToManagersRestaurant(restaurantId, IanaLinkRelations.SELF.value());
    }

    public Link linkToManagersRestaurantAssociation(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantUserController.class).associateManager(restaurantId, null))
                .withRel(rel)
                .withType(HttpMethod.PUT.name());
    }

    public Link linkToManagersRestaurantDisassociation(Long restaurantId, Long userId, String rel) {
        return linkTo(methodOn(RestaurantUserController.class).disassociateManager(restaurantId, userId))
                .withRel(rel)
                .withType(HttpMethod.DELETE.name());
    }

    public Link linkToOrderCancellation(String orderCode, String rel) {
        return linkTo(methodOn(OrderStatusController.class).cancellation(orderCode)).withRel(rel)
                .withType(HttpMethod.PUT.name());
    }

    public Link linkToOrderConfirmation(String orderCode, String rel) {
        return linkTo(methodOn(OrderStatusController.class).confirmation(orderCode)).withRel(rel)
                .withType(HttpMethod.PUT.name());
    }

    public Link linkToOrderDelivery(String orderCode, String rel) {
        return linkTo(methodOn(OrderStatusController.class).delivery(orderCode)).withRel(rel)
                .withType(HttpMethod.PUT.name());
    }

    public Link linkToOrder(String orderCode, String rel) {
        return linkTo(methodOn(OrderController.class).get(orderCode)).withRel(rel);
    }

    public Link linkToOrder(String orderCode) {
        return linkToOrder(orderCode, IanaLinkRelations.SELF.value());
    }

    public Link linkToOrders(String rel) {
        TemplateVariables variablesFilter = new TemplateVariables(
                new TemplateVariable("customerId", VariableType.REQUEST_PARAM),
                new TemplateVariable("restaurantId", VariableType.REQUEST_PARAM),
                new TemplateVariable("creationDateFirst", VariableType.REQUEST_PARAM),
                new TemplateVariable("creationDateLast", VariableType.REQUEST_PARAM));

        String ordersUrl = linkTo(OrderController.class).toUri().toString();

        return Link.of(UriTemplate.of(ordersUrl,
                PAGE_VARIABLES.concat(variablesFilter)), rel);
    }

    public Link linkToPaymentMethod(Long paymentMethodId, String rel) {
        return linkTo(methodOn(PaymentMethodController.class).get(paymentMethodId)).withRel(rel);
    }

    public Link linkToPaymentMethod(Long paymentMethodId) {
        return linkToPaymentMethod(paymentMethodId, IanaLinkRelations.SELF.value());
    }

    public Link linkToPaymentMethods(String rel) {
        return linkTo(methodOn(PaymentMethodController.class).list()).withRel(rel);
    }

    public Link linkToPaymentMethods() {
        return linkToPaymentMethods(IanaLinkRelations.SELF.value());
    }

    public Link linkToPermissions(String rel) {
        return linkTo(PermissionController.class).withRel(rel);
    }

    public Link linkToPermissions() {
        return linkToPermissions(IanaLinkRelations.SELF.value());
    }

    public Link linkToProduct(Long restaurantId, Long productId, String rel) {
        return linkTo(methodOn(RestaurantProductController.class).get(restaurantId, productId)).withRel(rel);
    }

    public Link linkToProduct(Long restaurantId, Long productId) {
        return linkToProduct(restaurantId, productId, IanaLinkRelations.SELF.value());
    }

    public Link linkToProductPhoto(Long restaurantId, Long productId, String rel) {
        return linkTo(methodOn(RestaurantProductPhotoController.class).getInfo(restaurantId, productId)).withRel(rel);
    }

    public Link linkToProductPhoto(Long restaurantId, Long productId) {
        return linkToProductPhoto(restaurantId, productId, IanaLinkRelations.SELF.value());
    }

    public Link linkToProducts(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantProductController.class).listAll(null, restaurantId)).withRel(rel);
    }

    public Link linkToProducts(Long restaurantId) {
        return linkToProducts(restaurantId, IanaLinkRelations.SELF.value());
    }

    public Link linkToRestaurant(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantController.class).get(restaurantId)).withRel(rel);
    }

    public Link linkToRestaurant(Long restaurantId) {
        return linkToRestaurant(restaurantId, IanaLinkRelations.SELF.value());
    }

    public Link linkToRestaurantOpening(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantController.class).open(restaurantId))
                .withRel(rel)
                .withType(HttpMethod.PUT.name());
    }

    public Link linkToRestaurantClosing(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantController.class).close(restaurantId))
                .withRel(rel)
                .withType(HttpMethod.PUT.name());
    }

    public Link linkToRestaurantActivation(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantController.class).activate(restaurantId))
                .withRel(rel)
                .withType(HttpMethod.PUT.name());
    }

    public Link linkToRestaurantInactivation(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantController.class).inactivate(restaurantId))
                .withRel(rel)
                .withType(HttpMethod.DELETE.name());
    }

    public Link linkToRestaurantPaymentMethods(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantPaymentMethodController.class).list(restaurantId)).withRel(rel);
    }

    public Link linkToRestaurantPaymentMethods(Long restaurantId) {
        return linkToRestaurantPaymentMethods(restaurantId, IanaLinkRelations.SELF.value());
    }

    public Link linkToRestaurantPaymentMethodAssociation(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantPaymentMethodController.class)
                .associate(restaurantId, null))
                .withRel(rel)
                .withType(HttpMethod.PUT.name());
    }

    public Link linkToRestaurantPaymentMethodDisassociation(Long restaurantId, Long paymentMethodId, String rel) {
        return linkTo(methodOn(RestaurantPaymentMethodController.class)
                .disassociate(restaurantId, paymentMethodId))
                .withRel(rel)
                .withType(HttpMethod.DELETE.name());
    }

    public Link linkToRestaurants(String rel) {
        String url = linkTo(RestaurantController.class).toUri().toString();
        return Link.of(UriTemplate.of(url, PROJECTION_VARIABLES), rel);
    }

    public Link linkToRestaurants() {
        return linkToRestaurants(IanaLinkRelations.SELF.value());
    }

    public Link linkToState(Long stateId, String rel) {
        return linkTo(methodOn(StateController.class).get(stateId)).withRel(rel);
    }

    public Link linkToState(Long stateId) {
        return linkToState(stateId, IanaLinkRelations.SELF.value());
    }

    public Link linkToStates(String rel) {
        return linkTo(StateController.class).withRel(rel);
    }

    public Link linkToStates() {
        return linkToStates(IanaLinkRelations.SELF.value());
    }

    public Link linkToStatistics(String rel) {
        return linkTo(StatisticsController.class).withRel(rel);
    }

    public Link linkToStatisticsDailySales(String rel) {
        TemplateVariables variablesFilter = new TemplateVariables(
                new TemplateVariable("restaurantId", VariableType.REQUEST_PARAM),
                new TemplateVariable("creationDateFirst", VariableType.REQUEST_PARAM),
                new TemplateVariable("creationDateLast", VariableType.REQUEST_PARAM),
                new TemplateVariable("timeOffset", VariableType.REQUEST_PARAM));

        String ordersUrl = linkTo(methodOn(StatisticsController.class)
                .list(null, null)).toUri().toString();

        return Link.of(UriTemplate.of(ordersUrl, variablesFilter), rel);
    }

    public Link linkToStatusOrder(String orderCode, String rel) {
        return linkTo(methodOn(OrderStatusController.class).getStatusInfo(orderCode)).withRel(rel);
    }

    public Link linkToStatusOrder(String orderCode) {
        return linkToStatusOrder(orderCode, IanaLinkRelations.SELF.value());
    }

    public Link linkToUser(Long userId, String rel) {
        return linkTo(methodOn(UserController.class).get(userId)).withRel(rel);
    }

    public Link linkToUser(Long userId) {
        return linkToUser(userId, IanaLinkRelations.SELF.value());
    }

    public Link linkToUsers(String rel) {
        return linkTo(UserController.class).withRel(rel);
    }

    public Link linkToUsers() {
        return linkToUsers(IanaLinkRelations.SELF.value());
    }

    public Link linkToUserGroup(Long userGroupId, String rel) {
        return linkTo(methodOn(UserGroupController.class).get(userGroupId)).withRel(rel);
    }

    public Link linkToUserGroup(Long userGroupId) {
        return linkToUserGroup(userGroupId, IanaLinkRelations.SELF.value());
    }

    public Link linkToUserGroups(String rel) {
        return linkTo(UserGroupController.class).withRel(rel);
    }

    public Link linkToUserGroups() {
        return linkToUserGroups(IanaLinkRelations.SELF.value());
    }

    public Link linkToUserGroupAssociation(Long userId, String rel) {
        return linkTo(methodOn(UserGroupUserController.class)
                .associate(userId, null))
                .withRel(rel)
                .withType(HttpMethod.PUT.name());
    }

    public Link linkToUserGroupDisassociation(Long userId, Long userGroupId, String rel) {
        return linkTo(methodOn(UserGroupUserController.class)
                .disassociate(userId, userGroupId))
                .withRel(rel)
                .withType(HttpMethod.DELETE.name());
    }

    public Link linkToUserGroupUser(Long userId, String rel) {
        return linkTo(methodOn(UserGroupUserController.class).list(userId)).withRel(rel);
    }

    public Link linkToUserGroupUser(Long userId) {
        return linkToUserGroupUser(userId, IanaLinkRelations.SELF.value());
    }

    public Link linkToUserGroupPermissions(Long userGroupId, String rel) {
        return linkTo(methodOn(UserGroupPermissionController.class).list(userGroupId)).withRel(rel);
    }

    public Link linkToUserGroupPermissions(Long userGroupId) {
        return linkToUserGroupPermissions(userGroupId, IanaLinkRelations.SELF.value());
    }

    public Link linkToUserGroupPermissionAssociation(Long userGroupId, String rel) {
        return linkTo(methodOn(UserGroupPermissionController.class)
                .associatePermission(userGroupId, null))
                .withRel(rel)
                .withType(HttpMethod.PUT.name());
    }

    public Link linkToUserGroupPermissionDisassociation(Long userGroupId, Long permissionId, String rel) {
        return linkTo(methodOn(UserGroupPermissionController.class)
                .disassociatePermission(userGroupId, permissionId))
                .withRel(rel)
                .withType(HttpMethod.DELETE.name());
    }
}