package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.ProductModel;
import com.ronijr.algafoodapi.config.mapper.ProductMapper;
import com.ronijr.algafoodapi.domain.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.api.hateoas.AlgaLinks.*;

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
        model.add(linkToProduct(product.getRestaurant().getId(), product.getId()));
        model.add(linkToProductPhoto(product.getRestaurant().getId(), product.getId(), "photo"));
        model.add(linkToProducts(product.getRestaurant().getId(), "product-list"));
        return model;
    }
}