package com.ronijr.algafoodapi.api.v1.assembler;

import com.ronijr.algafoodapi.api.v1.model.ProductModel;
import com.ronijr.algafoodapi.config.mapper.ProductMapper;
import com.ronijr.algafoodapi.config.security.AlgaSecurity;
import com.ronijr.algafoodapi.domain.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.*;

@Component
public class ProductAssembler extends RepresentationModelAssemblerSupport<Product, ProductModel.Output> {
    @Autowired
    private ProductMapper mapper;
    @Autowired
    private AlgaSecurity algaSecurity;

    public ProductAssembler() {
        super(Product.class, ProductModel.Output.class);
    }

    @Override
    public ProductModel.Output toModel(Product product) {
        ProductModel.Output model = mapper.entityToOutput(product);
        model.add(linkToProduct(product.getRestaurant().getId(), product.getId()));
        if (algaSecurity.allowQueryProducts()) {
            model.add(linkToProductPhoto(product.getRestaurant().getId(), product.getId(), "photo"));
            model.add(linkToProducts(product.getRestaurant().getId(), "product-list"));
        }
        return model;
    }
}