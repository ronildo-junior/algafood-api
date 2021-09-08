package com.ronijr.algafoodapi.api.model;

import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class OrderItemModel {
    private interface ProductId { @NotNull @Positive Long getProductId(); }
    private interface ProductName { String getProductName(); }
    private interface Amount { @NotNull @PositiveOrZero BigDecimal getAmount(); }
    private interface Price { @NotNull @PositiveOrZero BigDecimal getPrice(); }
    private interface Total { @NotNull @PositiveOrZero BigDecimal getTotal(); }
    private interface Notes { String getNotes(); }

    @Value
    public static class Input implements ProductId, Amount, Notes {
        Long productId;
        BigDecimal amount;
        String notes;
    }

    @Value
    public static class Output implements ProductId, ProductName, Amount, Price, Total, Notes {
        Long productId;
        String productName;
        BigDecimal amount;
        BigDecimal price;
        BigDecimal total;
        String notes;
    }
}