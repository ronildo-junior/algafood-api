package com.ronijr.algafoodapi.config.mapper;

import com.ronijr.algafoodapi.api.model.CityResource;
import com.ronijr.algafoodapi.domain.model.City;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CityMapper {
    City inputToEntity(CityResource.Input input);
    CityResource.Output entityToOutput(City entity);
    CityResource.Input entityToInput(City entity);
    CityResource.Summary entityToSummary(City entity);
    void mergeIntoTarget(CityResource.Input input, @MappingTarget City target);
}