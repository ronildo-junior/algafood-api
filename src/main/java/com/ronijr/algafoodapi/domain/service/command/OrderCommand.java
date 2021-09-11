package com.ronijr.algafoodapi.domain.service.command;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.BusinessException;
import com.ronijr.algafoodapi.domain.model.*;
import com.ronijr.algafoodapi.domain.repository.CityRepository;
import com.ronijr.algafoodapi.domain.repository.OrderRepository;
import com.ronijr.algafoodapi.domain.service.query.PaymentMethodQuery;
import com.ronijr.algafoodapi.domain.service.query.ProductQuery;
import com.ronijr.algafoodapi.domain.service.query.RestaurantQuery;
import com.ronijr.algafoodapi.domain.service.query.UserQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

@Service
@Transactional
@AllArgsConstructor
public class OrderCommand {
    private final CityRepository cityRepository;
    private final OrderRepository orderRepository;
    private final RestaurantQuery restaurantQuery;
    private final PaymentMethodQuery paymentMethodQuery;
    private final ProductQuery productQuery;
    private final UserQuery userQuery;
    private final AppMessageSource messageSource;

    public Order create(@Validated Order order) {
        verifyOrder(order);
        mergeEqualsItems(order);
        verifyItens(order);
        order.calculateTotal();
        return orderRepository.save(order);
    }

    private void verifyOrder(Order order) {
        City city = cityRepository.findByIdOrElseThrow(order.getDeliveryAddress().getCity().getId());
        User customer = userQuery.findByIdOrElseThrow(order.getCustomer().getId());
        PaymentMethod paymentMethod = paymentMethodQuery.findByIdOrElseThrow(order.getPaymentMethod().getId());
        Restaurant restaurant = restaurantQuery.findByIdOrElseThrow(order.getRestaurant().getId());
        if (!restaurant.acceptPaymentMethod(paymentMethod)) {
            throw new BusinessException(messageSource.getMessage("payment.method.restaurant.not.accept",
                    paymentMethod.getId(), restaurant.getId()));
        }
        order.getDeliveryAddress().setCity(city);
        order.setPaymentMethod(paymentMethod);
        order.setRestaurant(restaurant);
        order.setCustomer(customer);
    }

    private void verifyItens(Order order) {
        order.getItems().forEach(item -> {
            Product product = productQuery.findByIdOrElseThrow(item.getProduct().getId(), order.getRestaurant().getId());
            item.setOrder(order);
            item.setProduct(product);
        });
    }

    private void mergeEqualsItems(Order order) {
        Set<OrderItem> mergedItems = new HashSet<>();
        order.getItems().forEach(item ->
                mergedItems.stream().
                        filter(this.getOrderItemPredicate(item)).
                        findAny().
                        ifPresentOrElse(
                                newItem -> mergeItems(item, newItem),
                                () -> mergedItems.add(item))
        );
        order.setItems(mergedItems);
    }

    private void mergeItems(OrderItem source, OrderItem target) {
        target.setAmount(target.getAmount().add(source.getAmount()));
    }

    private Predicate<OrderItem> getOrderItemPredicate(OrderItem item) {
        return newItem ->
                newItem.getProduct().getId().equals(item.getProduct().getId()) &&
                newItem.getNotes().equals(item.getNotes());
    }
}