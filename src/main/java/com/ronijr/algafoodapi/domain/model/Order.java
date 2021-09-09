package com.ronijr.algafoodapi.domain.model;

import com.ronijr.algafoodapi.domain.exception.StatusTransitionException;
import com.ronijr.algafoodapi.domain.validation.ValidationGroups;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import javax.validation.groups.ConvertGroup;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString
public class Order extends AbstractEntity<Long> {
    @NotNull(groups = ValidationGroups.ValidateId.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

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
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.CREATED;

    private OffsetDateTime deliveredAt;
    private OffsetDateTime confirmedAt;
    private OffsetDateTime cancelledAt;

    @Valid
    @ConvertGroup(to = ValidationGroups.ValidateId.class)
    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private User customer;

    @Valid
    @ConvertGroup(to = ValidationGroups.ValidateId.class)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private PaymentMethod paymentMethod;

    @Valid
    @ConvertGroup(to = ValidationGroups.ValidateId.class)
    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Restaurant restaurant;

    @Size(min = 1)
    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderItem> items = new HashSet<>();

    @PrePersist
    private void setCode() {
        if (this.code == null) {
            this.setCode(this.getUUID().toString());
        }
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.deliveryFee = restaurant.getDeliveryFee();
    }

    private void recalculateItems() {
        this.getItems().forEach(OrderItem::calculateTotal);
    }

    private void calculateSubtotal() {
        this.recalculateItems();
        this.subtotal = items.stream().
                map(OrderItem::getTotal).
                reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void calculateTotal() {
        this.calculateSubtotal();
        this.total = this.subtotal.add(deliveryFee);
    }

    private void setStatus(OrderStatus newStatus) {
        if (!this.status.allowTransitionTo(newStatus)) {
            throw new StatusTransitionException(status.getValue(), newStatus.getValue());
        }
        this.status = newStatus;
    }

    public void confirm() {
        this.setStatus(OrderStatus.CONFIRMED);
        this.setConfirmedAt(OffsetDateTime.now());
    }

    public void cancel() {
        this.setStatus(OrderStatus.CANCELLED);
        this.setCancelledAt(OffsetDateTime.now());
    }

    public void delivery() {
        this.setStatus(OrderStatus.DELIVERED);
        this.setDeliveredAt(OffsetDateTime.now());
    }
}