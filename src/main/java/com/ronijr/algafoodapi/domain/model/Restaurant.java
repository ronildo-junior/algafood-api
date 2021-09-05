package com.ronijr.algafoodapi.domain.model;

import com.ronijr.algafoodapi.domain.validation.ValidationGroups;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.groups.ConvertGroup;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString
public class Restaurant extends AbstractEntity<Long> {
    @NotNull(groups = ValidationGroups.ValidateId.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Builder.Default
    @NotNull
    @Column(nullable = false)
    private Boolean active = Boolean.TRUE;

    @NotNull @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal deliveryFee;

    @Valid
    @NotNull
    @ConvertGroup(to = ValidationGroups.ValidateId.class)
    @ManyToOne
    @JoinColumn
    private Cuisine cuisine;

    @Valid
    @NotNull
    @Embedded
    private Address address;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "restaurant_payment_method",
        joinColumns = @JoinColumn(name = "restaurant_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_method_id"))
    private final List<PaymentMethod> paymentMethods = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "restaurant")
    private List<Product> products = new ArrayList<>();

    public void activate() {
        this.setActive(true);
    }

    public void inactivate() {
        this.setActive(false);
    }

    public void addPaymentMethod(PaymentMethod paymentMethod) {
        paymentMethods.add(paymentMethod);
    }
}