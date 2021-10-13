package com.ronijr.algafoodapi.api.v1.controller;

import com.ronijr.algafoodapi.api.v1.assembler.UserAssembler;
import com.ronijr.algafoodapi.api.v1.model.UserModel;
import com.ronijr.algafoodapi.config.security.CheckSecurity;
import com.ronijr.algafoodapi.domain.service.command.RestaurantCommand;
import com.ronijr.algafoodapi.domain.service.query.RestaurantQuery;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.*;

@RestController
@RequestMapping(value = "/v1/restaurants/{restaurantId}/managers",
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
@AllArgsConstructor
public class RestaurantUserController {
    private final RestaurantCommand restaurantCommand;
    private final RestaurantQuery restaurantQuery;
    private final UserAssembler userAssembler;

    @GetMapping
    public CollectionModel<UserModel.Output> list(@PathVariable Long restaurantId) {
        var collectionModel =
                userAssembler.toCollectionModel(restaurantQuery.getUsers(restaurantId))
                        .removeLinks()
                        .add(linkToManagersRestaurant(restaurantId))
                        .add(linkToManagersRestaurantAssociation(restaurantId, "associate"));
        collectionModel.forEach(user ->
            user.add(linkToManagersRestaurantDisassociation(restaurantId, user.getId(), "disassociate"))
        );
        return collectionModel;
    }

    @GetMapping("/{userId}")
    public UserModel.Output get(@PathVariable Long restaurantId, @PathVariable Long userId) {
        return userAssembler.toModel(restaurantQuery.getUser(restaurantId, userId));
    }

    @CheckSecurity.Restaurants.AllowEdit
    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associateManager(@PathVariable Long restaurantId, @PathVariable Long userId) {
        restaurantCommand.addManager(restaurantId, userId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurants.AllowEdit
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> disassociateManager(@PathVariable Long restaurantId, @PathVariable Long userId) {
        restaurantCommand.removeManager(restaurantId, userId);
        return ResponseEntity.noContent().build();
    }
}