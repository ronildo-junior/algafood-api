package com.ronijr.algafoodapi.config.mapper;

import com.ronijr.algafoodapi.api.v1.model.CityModel;
import com.ronijr.algafoodapi.domain.model.City;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CityMapper {
    City inputToEntity(CityModel.Input input);
    CityModel.Output entityToOutput(City entity);
    CityModel.Input entityToInput(City entity);
    CityModel.Summary entityToSummary(City entity);
    void mergeIntoTarget(CityModel.Input input, @MappingTarget City target);
}