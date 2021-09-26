package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.controller.CityController;
import com.ronijr.algafoodapi.api.controller.StateController;
import com.ronijr.algafoodapi.api.model.CityModel;
import com.ronijr.algafoodapi.config.mapper.CityMapper;
import com.ronijr.algafoodapi.domain.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        model.add(linkTo(methodOn(CityController.class).get(city.getId())).withSelfRel());
        model.add(linkTo(methodOn(CityController.class).list()).withRel("city-list"));
        model.getState().add(linkTo(methodOn(StateController.class).get(city.getState().getId())).withSelfRel());
        return model;
    }

    @Override
    public CollectionModel<CityModel.Output> toCollectionModel(Iterable<? extends City> cities) {
        return super.toCollectionModel(cities)
                .add(linkTo(CityController.class).withSelfRel());
    }
}