package com.ronijr.algafoodapi.api.v1.assembler;

import com.ronijr.algafoodapi.api.v1.model.ProductPhotoModel;
import com.ronijr.algafoodapi.config.mapper.ProductPhotoMapper;
import com.ronijr.algafoodapi.config.security.AlgaSecurity;
import com.ronijr.algafoodapi.domain.model.ProductPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.linkToProduct;
import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.linkToProductPhoto;

@Component
public class ProductPhotoAssembler extends RepresentationModelAssemblerSupport<ProductPhoto, ProductPhotoModel.Output> {
    @Autowired
    private ProductPhotoMapper mapper;
    @Autowired
    private AlgaSecurity algaSecurity;

    public ProductPhotoAssembler() {
        super(ProductPhoto.class, ProductPhotoModel.Output.class);
    }

    public ProductPhotoModel.Output toModel(ProductPhoto productPhoto) {
        ProductPhotoModel.Output model = mapper.entityToOutput(productPhoto);
        if (algaSecurity.allowQueryProducts()) {
            model.add(linkToProductPhoto(
                    productPhoto.getProduct().getRestaurant().getId(), productPhoto.getProduct().getId()));
            model.add(linkToProduct(
                    productPhoto.getProduct().getRestaurant().getId(), productPhoto.getProduct().getId(), "product"));
        }
        return model;
    }
}