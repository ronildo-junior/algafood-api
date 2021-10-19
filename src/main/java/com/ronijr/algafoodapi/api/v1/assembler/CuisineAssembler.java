package com.ronijr.algafoodapi.api.v1.assembler;

import com.ronijr.algafoodapi.api.v1.model.CuisineModel;
import com.ronijr.algafoodapi.config.mapper.CuisineMapper;
import com.ronijr.algafoodapi.config.security.AlgaSecurity;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.linkToCuisine;
import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.linkToCuisines;

@Component
public class CuisineAssembler extends RepresentationModelAssemblerSupport<Cuisine, CuisineModel.Output> {
    @Autowired
    private CuisineMapper mapper;
    @Autowired
    private AlgaSecurity algaSecurity;

    public CuisineAssembler() {
        super(Cuisine.class, CuisineModel.Output.class);
    }

    @Override
    public CuisineModel.Output toModel(Cuisine cuisine) {
        CuisineModel.Output model = mapper.entityToOutput(cuisine);
        model.add(linkToCuisine(cuisine.getId()));
        if (algaSecurity.allowQueryCuisines()) {
            model.add(linkToCuisines("cuisine-list"));
        }
        return model;
    }

    @Override
    public CollectionModel<CuisineModel.Output> toCollectionModel(Iterable<? extends Cuisine> entities) {
        return super.toCollectionModel(entities).add(linkToCuisines());
    }
}