package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.PaymentMethodAssembler;
import com.ronijr.algafoodapi.api.model.PaymentMethodModel;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import com.ronijr.algafoodapi.domain.service.command.RestaurantCommand;
import com.ronijr.algafoodapi.domain.service.query.RestaurantQuery;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ronijr.algafoodapi.api.hateoas.AlgaLinks.*;

@RestController
@RequestMapping(value = "/restaurants/{restaurantId}/payment-methods", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class RestaurantPaymentMethodController {
    private final RestaurantQuery queryService;
    private final RestaurantCommand restaurantCommand;
    private final PaymentMethodAssembler paymentMethodAssembler;

    @GetMapping
    public CollectionModel<PaymentMethodModel.Output> list(@PathVariable Long restaurantId){
        Restaurant restaurant = queryService.findByIdOrElseThrow(restaurantId);
        var collectionModel =
                paymentMethodAssembler.toCollectionModel(restaurant.getPaymentMethods())
                        .removeLinks()
                        .add(linkToRestaurantPaymentMethods(restaurantId))
                        .add(linkToRestaurantPaymentMethodAssociation(restaurantId, "associate"));
        collectionModel.getContent().forEach(paymentMethod ->
            paymentMethod
                    .add(linkToRestaurantPaymentMethodDisassociation(restaurantId, paymentMethod.getId(), "disassociate"))
        );
        return collectionModel;
    }

    @PutMapping("/{paymentMethodId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associate(@PathVariable Long restaurantId, @PathVariable Long paymentMethodId) {
        restaurantCommand.associatePaymentMethod(restaurantId, paymentMethodId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{paymentMethodId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> disassociate(@PathVariable Long restaurantId, @PathVariable Long paymentMethodId) {
        restaurantCommand.disassociatePaymentMethod(restaurantId, paymentMethodId);
        return ResponseEntity.noContent().build();
    }
}