package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.CuisineModel;
import com.ronijr.algafoodapi.config.mapper.CuisineMapper;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CuisineAssembler {
    private final CuisineMapper mapper;

    public CuisineModel.Output toOutput(Cuisine cuisine) {
        return mapper.entityToOutput(cuisine);
    }

    public CuisineModel.Input toInput(Cuisine cuisine) {
        return mapper.entityToInput(cuisine);
    }

    public List<CuisineModel.Output> toCollectionModel(List<Cuisine> cuisines) {
        return cuisines.stream().
                map(this::toOutput).
                collect(Collectors.toList());
    }
}