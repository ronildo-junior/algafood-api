package com.ronijr.algafoodapi.config.mapper;

import com.ronijr.algafoodapi.api.model.CuisineResource;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CuisineMapper {
    Cuisine inputToEntity(CuisineResource.Input input);
    CuisineResource.Output entityToOutput(Cuisine entity);
}