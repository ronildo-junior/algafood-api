package com.ronijr.algafoodapi.api.v1.assembler;

import com.ronijr.algafoodapi.api.v1.model.CityModel;
import com.ronijr.algafoodapi.config.mapper.CityMapper;
import com.ronijr.algafoodapi.domain.model.City;
import com.ronijr.algafoodapi.domain.model.State;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CityDisassembler {
    private final CityMapper mapper;

    public City toDomain(CityModel.Input input) {
        return mapper.inputToEntity(input);
    }

    /**
     * Its necessary detach nested entity or replace by new entity non managed by hibernate,
     * to avoid invoke entity manager in this context, I choose set New Entity.
     * */
    public void copyToDomainObject(CityModel.Input input, City city) {
        city.setState(new State());
        mapper.mergeIntoTarget(input, city);
    }
}