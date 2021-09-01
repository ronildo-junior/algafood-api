package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.CityResource;
import com.ronijr.algafoodapi.config.mapper.CityMapper;
import com.ronijr.algafoodapi.domain.model.City;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CityAssembler {
    private final CityMapper mapper;

    public CityResource.Summary toSummary(City city) {
        return mapper.entityToSummary(city);
    }

    public CityResource.Output toOutput(City city) {
        return mapper.entityToOutput(city);
    }

    public CityResource.Input toInput(City city) {
        return mapper.entityToInput(city);
    }

    public List<CityResource.Summary> toCollectionModel(List<City> cities) {
        return cities.stream().
                map(this::toSummary).
                collect(Collectors.toList());
    }
}