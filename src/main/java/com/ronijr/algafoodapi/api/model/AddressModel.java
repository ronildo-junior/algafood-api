package com.ronijr.algafoodapi.api.model;

import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public final class AddressModel {
    private interface PostalCode { @NotBlank String getPostalCode (); }
    private interface AddressName { @NotBlank String getAddressName(); }
    private interface Number { @NotBlank String getNumber(); }
    private interface Complement { String getComplement(); }
    private interface Neighborhood { @NotBlank String getNeighborhood(); }
    private interface CityIdentifier { @NotNull @Valid CityModel.Identifier getCity(); }
    private interface CitySummary { @NotNull CityModel.Summary getCity(); }

    @Value
    public static class Input
            implements PostalCode, AddressName, Number, Complement, Neighborhood, CityIdentifier {
        String postalCode;
        String addressName;
        String number;
        String complement;
        String neighborhood;
        CityModel.Identifier city;
    }

    @Value
    public static class Output
            implements PostalCode, AddressName, Number, Complement, Neighborhood, CitySummary {
        String postalCode;
        String addressName;
        String number;
        String complement;
        String neighborhood;
        CityModel.Summary city;
    }
}