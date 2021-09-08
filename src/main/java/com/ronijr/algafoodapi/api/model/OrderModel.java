package com.ronijr.algafoodapi.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public final class OrderModel {
    private interface Id { @NotNull @Positive Long getId(); }
    private interface CanceledAt { OffsetDateTime getCanceledAt(); }
    private interface CreatedAt { OffsetDateTime getCreatedAt(); }
    private interface ConfirmedAt { OffsetDateTime getConfirmedAt(); }
    private interface Customer { UserModel.Output getCustomer(); }
    private interface CustomerIdentifier { @NotNull @Valid UserModel.Identifier getCustomer(); }
    private interface DeliveryAddress { @NotNull AddressModel.Output getDeliveryAddress(); }
    private interface DeliveryAddressInput { @NotNull @Valid AddressModel.Input getDeliveryAddress(); }
    private interface DeliveryFee { @NotNull @PositiveOrZero BigDecimal getDeliveryFee(); }
    private interface DeliveredAt { OffsetDateTime getDeliveredAt(); }
    private interface ItemList { List<OrderItemModel.Output> getItems(); }
    private interface ItemListInput { @NotNull @Size(min = 1) @Valid List<OrderItemModel.Input> getItems(); }
    private interface Status { String getStatus(); }
    private interface Subtotal { @NotNull @PositiveOrZero BigDecimal getSubtotal(); }
    private interface Restaurant { RestaurantModel.Simple getRestaurant(); }
    private interface RestaurantIdentifier { @NotNull @Valid RestaurantModel.Identifier getRestaurant();}
    private interface PaymentMethod { PaymentMethodModel.Output getPaymentMethod(); }
    private interface PaymentMethodIdentifier { @NotNull @Valid PaymentMethodModel.Identifier getPaymentMethod(); }
    private interface Total { @NotNull @PositiveOrZero BigDecimal getTotal(); }

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
    public static class Output implements
            Id, CreatedAt, ConfirmedAt, CanceledAt, DeliveryFee, DeliveredAt, Status, Subtotal, Total,
            Customer, Restaurant, PaymentMethod, DeliveryAddress, ItemList {
        Long id;
        OffsetDateTime createdAt;
        OffsetDateTime confirmedAt;
        OffsetDateTime canceledAt;
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
    public static class Summary implements
            Id, CreatedAt, DeliveryFee, Status, Subtotal, Total, Customer, Restaurant {
        Long id;
        OffsetDateTime createdAt;
        String status;
        BigDecimal deliveryFee;
        BigDecimal subtotal;
        BigDecimal total;
        RestaurantModel.Simple restaurant;
        UserModel.Output customer;
    }
}