package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.controller.RestaurantProductController;
import com.ronijr.algafoodapi.api.model.ProductModel;
import com.ronijr.algafoodapi.config.mapper.ProductMapper;
import com.ronijr.algafoodapi.domain.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductAssembler extends RepresentationModelAssemblerSupport<Product, ProductModel.Output> {
    @Autowired
    private ProductMapper mapper;

    public ProductAssembler() {
        super(Product.class, ProductModel.Output.class);
    }

    @Override
    public ProductModel.Output toModel(Product product) {
        ProductModel.Output model = mapper.entityToOutput(product);
        model.add(linkTo(methodOn(RestaurantProductController.class)
                .get(product.getRestaurant().getId(), product.getId()))
                .withSelfRel());
        model.add(linkTo(methodOn(RestaurantProductController.class)
                .listAll(true, product.getRestaurant().getId()))
                .withRel("product-list"));
        return model;
    }

    public CollectionModel<ProductModel.Output> toCollectionModel(Iterable<? extends Product> products) {
        return super.toCollectionModel(products)
                .add(linkTo(RestaurantProductController.class).withSelfRel());
    }
}