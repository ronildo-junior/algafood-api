package com.ronijr.algafoodapi.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString
public class Restaurant extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal deliveryFee;

    @ManyToOne
    @JoinColumn
    private Cuisine cuisine;

    @JsonIgnore
    @Embedded
    private Address address;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "restaurant_payment_method",
        joinColumns = @JoinColumn(name = "restaurant_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_method_id"))
    Set<PaymentMethod> paymentMethods = new HashSet<>();

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "restaurant")
    private List<Product> products = new ArrayList<>();
}
