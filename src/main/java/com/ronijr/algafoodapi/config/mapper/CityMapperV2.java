package com.ronijr.algafoodapi.config.mapper;

import com.ronijr.algafoodapi.api.v2.model.CityModel;
import com.ronijr.algafoodapi.domain.model.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CityMapperV2 {
    @Mapping(target="name", source="input.cityName")
    @Mapping(target="state.id", source="input.stateId")
    City inputToEntity(CityModel.InputV2 input);

    @Mapping(target="cityId", source="entity.id")
    @Mapping(target="cityName", source="entity.name")
    @Mapping(target="stateId", source="entity.state.id")
    @Mapping(target="stateName", source="entity.state.name")
    CityModel.OutputV2 entityToOutput(City entity);

    @Mapping(target="cityId", source="entity.id")
    @Mapping(target="cityName", source="entity.name")
    CityModel.SummaryV2 entityToSummary(City entity);

    @Mapping(target="target.name", source="input.cityName")
    @Mapping(target="target.state.id", source="input.stateId")
    void mergeIntoTarget(CityModel.InputV2 input, @MappingTarget City target);
}