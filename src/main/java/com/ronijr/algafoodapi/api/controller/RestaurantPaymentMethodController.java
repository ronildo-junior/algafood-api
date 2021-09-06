package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.PaymentMethodAssembler;
import com.ronijr.algafoodapi.api.model.PaymentMethodModel;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import com.ronijr.algafoodapi.domain.service.RestaurantCommand;
import com.ronijr.algafoodapi.domain.service.RestaurantQuery;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants/{restaurantId}/payment-methods")
@AllArgsConstructor
public class RestaurantPaymentMethodController {
    private final RestaurantQuery queryService;
    private final RestaurantCommand restaurantCommand;
    private final PaymentMethodAssembler paymentMethodAssembler;

    @GetMapping
    public List<PaymentMethodModel.Output> list(@PathVariable Long restaurantId){
        Restaurant restaurant = queryService.findByIdOrElseThrow(restaurantId);
        return paymentMethodAssembler.toCollectionModel(restaurant.getPaymentMethods());
    }

    @PutMapping("/{paymentMethodId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associate(@PathVariable Long restaurantId, @PathVariable Long paymentMethodId) {
        restaurantCommand.associatePaymentMethod(restaurantId, paymentMethodId);
    }

    @DeleteMapping("/{paymentMethodId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disassociate(@PathVariable Long restaurantId, @PathVariable Long paymentMethodId) {
        restaurantCommand.disassociatePaymentMethod(restaurantId, paymentMethodId);
    }
}