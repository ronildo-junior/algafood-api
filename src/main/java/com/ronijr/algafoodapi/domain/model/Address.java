package com.ronijr.algafoodapi.domain.model;

import com.ronijr.algafoodapi.domain.validation.ValidationGroups;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Address {
    @Column(name = "address_postal_code")
    private String postalCode;

    @Column(name = "address_name")
    private String addressName;

    @Column(name = "address_number")
    private String number;

    @Column(name = "address_complement")
    private String complement;

    @Column(name = "address_neighborhood")
    private String neighborhood;

    @Valid
    @ConvertGroup(to = ValidationGroups.ValidateId.class)
    @NotNull
    @ManyToOne
    @JoinColumn(name = "address_city_id")
    private City city;
}
