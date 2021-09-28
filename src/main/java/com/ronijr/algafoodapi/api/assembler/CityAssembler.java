package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.CityModel;
import com.ronijr.algafoodapi.config.mapper.CityMapper;
import com.ronijr.algafoodapi.domain.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.api.hateoas.AlgaLinks.*;

@Component
public class CityAssembler extends RepresentationModelAssemblerSupport<City, CityModel.Output> {
    @Autowired
    private CityMapper mapper;

    public CityAssembler() {
        super(City.class, CityModel.Output.class);
    }

    @Override
    public CityModel.Output toModel(City city) {
        CityModel.Output model = mapper.entityToOutput(city);
        model.add(linkToCity(city.getId()));
        model.add(linkToCities("city-list"));
        model.getState().add(linkToState(city.getState().getId()));
        return model;
    }

    @Override
    public CollectionModel<CityModel.Output> toCollectionModel(Iterable<? extends City> cities) {
        return super.toCollectionModel(cities).add(linkToCities());
    }
}