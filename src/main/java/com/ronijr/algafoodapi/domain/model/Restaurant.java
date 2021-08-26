package com.ronijr.algafoodapi.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import java.util.HashSet;
import java.util.List;
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

    @PositiveOrZero
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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
