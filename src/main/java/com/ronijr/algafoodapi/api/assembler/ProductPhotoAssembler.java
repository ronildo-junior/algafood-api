package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.ProductPhotoModel;
import com.ronijr.algafoodapi.config.mapper.ProductPhotoMapper;
import com.ronijr.algafoodapi.domain.model.ProductPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.api.hateoas.AlgaLinks.*;

@Component
public class ProductPhotoAssembler extends RepresentationModelAssemblerSupport<ProductPhoto, ProductPhotoModel.Output> {
    @Autowired
    private ProductPhotoMapper mapper;

    public ProductPhotoAssembler() {
        super(ProductPhoto.class, ProductPhotoModel.Output.class);
    }

    public ProductPhotoModel.Output toModel(ProductPhoto productPhoto) {
        ProductPhotoModel.Output model = mapper.entityToOutput(productPhoto);
        model.add(linkToProductPhoto(
                productPhoto.getProduct().getRestaurant().getId(), productPhoto.getProduct().getId()));
        model.add(linkToProduct(
                productPhoto.getProduct().getRestaurant().getId(), productPhoto.getProduct().getId(), "product"));
        return model;
    }
}