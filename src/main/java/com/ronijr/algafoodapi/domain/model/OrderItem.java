package com.ronijr.algafoodapi.domain.model;

import com.ronijr.algafoodapi.domain.validation.ValidationGroups;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.groups.ConvertGroup;
import java.math.BigDecimal;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString
public class OrderItem extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @PositiveOrZero
    private BigDecimal amount;
    @NotNull @PositiveOrZero
    private BigDecimal price;
    @NotNull @PositiveOrZero
    private BigDecimal total;

    private String notes;

    @Valid
    @ConvertGroup(to = ValidationGroups.ValidateId.class)
    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;

    @Valid
    @ConvertGroup(to = ValidationGroups.ValidateId.class)
    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Order order;

    public BigDecimal getPrice() {
        if (this.price == null) {
            this.price = BigDecimal.ZERO;
        }
        return this.price;
    }

    public BigDecimal getAmount() {
        if (this.amount == null) {
            this.amount = BigDecimal.ZERO;
        }
        return this.amount;
    }

    public void calculateTotal() {
        this.setTotal(this.getPrice().multiply(this.getAmount()));
    }

    public void setProduct(Product product) {
        this.product = product;
        this.price = product.getPrice();
    }
}