package com.ronijr.algafoodapi.config.mapper;

import com.ronijr.algafoodapi.api.model.CuisineModel;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CuisineMapper {
    Cuisine inputToEntity(CuisineModel.Input input);
    CuisineModel.Output entityToOutput(Cuisine entity);
    CuisineModel.Input entityToInput(Cuisine entity);
    void mergeIntoTarget(CuisineModel.Input input, @MappingTarget Cuisine target);
}