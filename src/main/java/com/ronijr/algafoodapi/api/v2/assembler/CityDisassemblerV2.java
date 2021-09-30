package com.ronijr.algafoodapi.api.v2.assembler;

import com.ronijr.algafoodapi.api.v2.model.CityModel;
import com.ronijr.algafoodapi.config.mapper.CityMapperV2;
import com.ronijr.algafoodapi.domain.model.City;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CityDisassemblerV2 {
    private final CityMapperV2 mapper;

    public City toDomain(CityModel.InputV2 input) {
        return mapper.inputToEntity(input);
    }

    public void copyToDomainObject(CityModel.InputV2 input, City city) {
        mapper.mergeIntoTarget(input, city);
    }
}