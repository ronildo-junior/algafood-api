package com.ronijr.algafoodapi.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
@Getter
@Setter
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
    @ManyToOne
    @JoinColumn
    private City city;
}
