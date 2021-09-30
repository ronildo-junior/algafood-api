package com.ronijr.algafoodapi.api.v2.assembler;

import com.ronijr.algafoodapi.api.v2.model.CityModel;
import com.ronijr.algafoodapi.config.mapper.CityMapperV2;
import com.ronijr.algafoodapi.domain.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.api.v2.hateoas.AlgaLinksV2.linkToCities;
import static com.ronijr.algafoodapi.api.v2.hateoas.AlgaLinksV2.linkToCity;

@Component
public class CitySummaryAssemblerV2 extends RepresentationModelAssemblerSupport<City, CityModel.SummaryV2> {
    @Autowired
    private CityMapperV2 mapper;

    public CitySummaryAssemblerV2() {
        super(City.class, CityModel.SummaryV2.class);
    }

    @Override
    public CityModel.SummaryV2 toModel(City city) {
        CityModel.SummaryV2 model = mapper.entityToSummary(city);
        model.add(linkToCity(city.getId()));
        model.add(linkToCities("city-list"));
        return model;
    }

    @Override
    public CollectionModel<CityModel.SummaryV2> toCollectionModel(Iterable<? extends City> cities) {
        return super.toCollectionModel(cities).add(linkToCities());
    }
}