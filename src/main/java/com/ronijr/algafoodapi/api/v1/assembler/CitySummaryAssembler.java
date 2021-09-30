package com.ronijr.algafoodapi.api.v1.assembler;

import com.ronijr.algafoodapi.api.v1.model.CityModel;
import com.ronijr.algafoodapi.config.mapper.CityMapper;
import com.ronijr.algafoodapi.domain.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.linkToCities;
import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.linkToCity;

@Component
public class CitySummaryAssembler extends RepresentationModelAssemblerSupport<City, CityModel.Summary> {
    @Autowired
    private CityMapper mapper;

    public CitySummaryAssembler() {
        super(City.class, CityModel.Summary.class);
    }

    @Override
    public CityModel.Summary toModel(City city) {
        CityModel.Summary model = mapper.entityToSummary(city);
        model.add(linkToCity(city.getId()));
        model.add(linkToCities("city-list"));
        return model;
    }

    @Override
    public CollectionModel<CityModel.Summary> toCollectionModel(Iterable<? extends City> cities) {
        return super.toCollectionModel(cities).add(linkToCities());
    }
}