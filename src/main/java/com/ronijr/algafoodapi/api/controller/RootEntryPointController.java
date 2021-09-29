package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.hateoas.RootEntryPointModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ronijr.algafoodapi.api.hateoas.AlgaLinks.*;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
public class RootEntryPointController {
    @GetMapping
    public RootEntryPointModel root() {
        var rootEntry = new RootEntryPointModel();
        rootEntry.add(linkToCities("cities"));
        rootEntry.add(linkToCuisines("cuisines"));
        rootEntry.add(linkToOrders("orders"));
        rootEntry.add(linkToPaymentMethods("payment-methods"));
        rootEntry.add(linkToPermissions("permissions"));
        rootEntry.add(linkToProducts(null, "products"));
        rootEntry.add(linkToRestaurants("restaurants"));
        rootEntry.add(linkToStates("states"));
        rootEntry.add(linkToStatistics("statistics"));
        rootEntry.add(linkToUsers("users"));
        rootEntry.add(linkToUserGroups("user-groups"));
        return rootEntry;
    }
}