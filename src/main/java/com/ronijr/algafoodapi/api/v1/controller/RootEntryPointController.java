package com.ronijr.algafoodapi.api.v1.controller;

import com.ronijr.algafoodapi.api.v1.hateoas.RootEntryPointModel;
import com.ronijr.algafoodapi.config.security.AlgaSecurity;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/v1", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
public class RootEntryPointController {
    private final AlgaSecurity algaSecurity;
    @GetMapping
    public RootEntryPointModel root() {
        var rootEntry = new RootEntryPointModel();
        if (algaSecurity.allowQueryCities()) {
            rootEntry.add(linkToCities("cities"));
        }
        if (algaSecurity.allowQueryCuisines()) {
            rootEntry.add(linkToCuisines("cuisines"));
        }
        if (algaSecurity.allowQueryOrders()) {
            rootEntry.add(linkToOrders("orders"));
            rootEntry.add(linkToStatistics("statistics"));
        }
        if (algaSecurity.allowQueryPaymentMethods()) {
            rootEntry.add(linkToPaymentMethods("payment-methods"));
        }
        if (algaSecurity.allowQueryProducts()) {
            rootEntry.add(linkToProducts(null, "products"));
        }
        if (algaSecurity.allowQueryRestaurants()) {
            rootEntry.add(linkToRestaurants("restaurants"));
        }
        if (algaSecurity.allowQueryRestaurants()) {
            rootEntry.add(linkToStates("states"));
        }
        if (algaSecurity.allowQueryUsers()) {
            rootEntry.add(linkToUsers("users"));
        }
        if (algaSecurity.allowQueryUserGroupPermissions()) {
            rootEntry.add(linkToUserGroups("user-groups"));
            rootEntry.add(linkToPermissions("permissions"));
        }
        return rootEntry;
    }
}