package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.UserAssembler;
import com.ronijr.algafoodapi.api.model.UserModel;
import com.ronijr.algafoodapi.domain.service.command.RestaurantCommand;
import com.ronijr.algafoodapi.domain.service.query.RestaurantQuery;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/restaurants/{restaurantId}/managers", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class RestaurantUserController {
    private final RestaurantCommand restaurantCommand;
    private final RestaurantQuery restaurantQuery;
    private final UserAssembler userAssembler;

    @GetMapping
    public CollectionModel<UserModel.Output> list(@PathVariable Long restaurantId) {
        return userAssembler.toCollectionModel(restaurantQuery.getUsers(restaurantId));
    }

    @GetMapping("/{userId}")
    public UserModel.Output get(@PathVariable Long restaurantId, @PathVariable Long userId) {
        return userAssembler.toModel(restaurantQuery.getUser(restaurantId, userId));
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associateManager(@PathVariable Long restaurantId, @PathVariable Long userId) {
        restaurantCommand.addManager(restaurantId, userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disassociateManager(@PathVariable Long restaurantId, @PathVariable Long userId) {
        restaurantCommand.removeManager(restaurantId, userId);
    }
}