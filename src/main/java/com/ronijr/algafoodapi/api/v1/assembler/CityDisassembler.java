package com.ronijr.algafoodapi.api.v1.assembler;

import com.ronijr.algafoodapi.api.v1.model.CityModel;
import com.ronijr.algafoodapi.config.mapper.CityMapper;
import com.ronijr.algafoodapi.domain.model.City;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CityDisassembler {
    private final CityMapper mapper;

    public City toDomain(CityModel.Input input) {
        return mapper.inputToEntity(input);
    }

    public void copyToDomainObject(CityModel.Input input, City city) {
        mapper.mergeIntoTarget(input, city);
    }
}