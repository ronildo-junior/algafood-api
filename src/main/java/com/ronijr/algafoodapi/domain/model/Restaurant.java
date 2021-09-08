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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    @Builder.Default
    @NotNull
    @Column(nullable = false)
    private Boolean opened = Boolean.FALSE;

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
    private final Set<PaymentMethod> paymentMethods = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "restaurant")
    private final Set<Product> products = new HashSet<>();

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "restaurant_manager",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private final Set<User> users = new HashSet<>();

    public Optional<Product> getProduct(Long id) {
        return products.stream().
                filter(product -> product.getId().equals(id))
                .findAny();
    }

    public Optional<User> getUser(Long id) {
        return users.stream().
                filter(r -> r.getId().equals(id)).
                findAny();
    }

    public boolean acceptPaymentMethod(PaymentMethod paymentMethod) {
        return this.paymentMethods.contains(paymentMethod);
    }

    public void activate() {
        this.setActive(true);
    }

    public void inactivate() {
        this.setActive(false);
    }

    public void open() {
        this.setOpened(true);
    }

    public void close() {
        this.setOpened(false);
    }

    public void addPaymentMethod(PaymentMethod paymentMethod) {
        paymentMethods.add(paymentMethod);
    }

    public void removePaymentMethod(PaymentMethod paymentMethod) {
        paymentMethods.remove(paymentMethod);
    }

    public void addManager(User user) {
        users.add(user);
    }

    public void removeManager(User user) {
        users.remove(user);
    }
}