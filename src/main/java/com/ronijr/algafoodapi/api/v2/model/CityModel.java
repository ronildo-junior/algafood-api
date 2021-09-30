package com.ronijr.algafoodapi.api.v2.model;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public final class CityModel {
    private interface Id { @NotNull @Positive Long getCityId(); }
    private interface Name { @NotBlank String getCityName(); }
    private interface StateId { @NotNull @Positive Long getStateId(); }

    @Value
    public static class IdentifierV2 implements Id {
        Long cityId;
    }

    @Value
    public static class InputV2 implements Name, StateId {
        String cityName;
        Long stateId;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    public static class SummaryV2 extends RepresentationModel<SummaryV2> {
        Long cityId;
        String cityName;
    }

    @EqualsAndHashCode(callSuper = true)
    @Value
    public static class OutputV2 extends RepresentationModel<OutputV2> {
        Long cityId;
        String cityName;
        Long stateId;
        String stateName;
    }
}