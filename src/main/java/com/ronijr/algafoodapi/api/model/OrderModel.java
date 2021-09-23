package com.ronijr.algafoodapi.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public final class OrderModel {
    private interface CustomerIdentifier { @NotNull @Valid UserModel.Identifier getCustomer(); }
    private interface DeliveryAddressInput { @NotNull @Valid AddressModel.Input getDeliveryAddress(); }
    private interface ItemListInput { @NotNull @Size(min = 1) @Valid List<OrderItemModel.Input> getItems(); }
    private interface RestaurantIdentifier { @NotNull @Valid RestaurantModel.Identifier getRestaurant();}
    private interface PaymentMethodIdentifier { @NotNull @Valid PaymentMethodModel.Identifier getPaymentMethod(); }

    @Value
    public static class Input implements
            CustomerIdentifier, RestaurantIdentifier, PaymentMethodIdentifier, DeliveryAddressInput, ItemListInput {
        UserModel.Identifier customer;
        RestaurantModel.Identifier restaurant;
        PaymentMethodModel.Identifier paymentMethod;
        AddressModel.Input deliveryAddress;
        List<OrderItemModel.Input> items;
    }

    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Output {
        String code;
        OffsetDateTime createdAt;
        OffsetDateTime confirmedAt;
        OffsetDateTime cancelledAt;
        OffsetDateTime deliveredAt;
        String status;
        BigDecimal deliveryFee;
        BigDecimal subtotal;
        BigDecimal total;
        RestaurantModel.Simple restaurant;
        UserModel.Output customer;
        PaymentMethodModel.Output paymentMethod;
        AddressModel.Output deliveryAddress;
        List<OrderItemModel.Output> items;
    }

    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Summary {
        String code;
        OffsetDateTime createdAt;
        String status;
        BigDecimal deliveryFee;
        BigDecimal subtotal;
        BigDecimal total;
        RestaurantModel.Simple restaurant;
        UserModel.Output customer;
    }

    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class StatusInfo {
        String status;
        OffsetDateTime createdAt;
        OffsetDateTime deliveredAt;
        OffsetDateTime cancelledAt;
    }
}