package com.ronijr.algafoodapi.domain.model;

import com.ronijr.algafoodapi.domain.validation.ValidationGroups;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.groups.ConvertGroup;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString
public class Order extends AbstractEntity<Long> {
    @NotNull(groups = ValidationGroups.ValidateId.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @PositiveOrZero
    private BigDecimal deliveryFee;
    @NotNull @PositiveOrZero
    private BigDecimal subtotal;
    @NotNull @PositiveOrZero
    private BigDecimal total;

    @Valid
    @NotNull
    @Embedded
    private Address deliveryAddress;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @NotNull
    @Column(nullable = false)
    private OffsetDateTime deliveryDate;
    private OffsetDateTime confirmedAt;
    private OffsetDateTime cancelledAt;

    @Valid
    @ConvertGroup(to = ValidationGroups.ValidateId.class)
    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Valid
    @ConvertGroup(to = ValidationGroups.ValidateId.class)
    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private PaymentMethod paymentMethod;

    @Valid
    @ConvertGroup(to = ValidationGroups.ValidateId.class)
    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Restaurant restaurant;

    @ToString.Exclude
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();
}