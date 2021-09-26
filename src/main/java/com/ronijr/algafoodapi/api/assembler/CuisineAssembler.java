package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.controller.CuisineController;
import com.ronijr.algafoodapi.api.model.CuisineModel;
import com.ronijr.algafoodapi.config.mapper.CuisineMapper;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CuisineAssembler extends RepresentationModelAssemblerSupport<Cuisine, CuisineModel.Output> {
    @Autowired
    private CuisineMapper mapper;

    public CuisineAssembler() {
        super(Cuisine.class, CuisineModel.Output.class);
    }

    @Override
    public CuisineModel.Output toModel(Cuisine cuisine) {
        CuisineModel.Output model = mapper.entityToOutput(cuisine);
        model.add(linkTo(methodOn(CuisineController.class).get(model.getId())).withSelfRel());
        model.add(linkTo(methodOn(CuisineController.class).list()).withRel("cuisine-list"));
        return model;
    }

    @Override
    public CollectionModel<CuisineModel.Output> toCollectionModel(Iterable<? extends Cuisine> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(CuisineController.class).withSelfRel());
    }
}