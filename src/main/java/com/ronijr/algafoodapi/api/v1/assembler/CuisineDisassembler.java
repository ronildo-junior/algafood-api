package com.ronijr.algafoodapi.api.v1.assembler;

import com.ronijr.algafoodapi.api.v1.model.CuisineModel;
import com.ronijr.algafoodapi.config.mapper.CuisineMapper;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CuisineDisassembler {
    private final CuisineMapper mapper;

    public Cuisine toDomain(CuisineModel.Input input) {
        return mapper.inputToEntity(input);
    }

    public void copyToDomainObject(CuisineModel.Input input, Cuisine cuisine) {
        mapper.mergeIntoTarget(input, cuisine);
    }
}