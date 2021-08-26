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

    @PositiveOrZero
    private BigDecimal amount;
    @PositiveOrZero
    private BigDecimal price;
    @PositiveOrZero
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
}
