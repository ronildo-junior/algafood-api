package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.ProductModel;
import com.ronijr.algafoodapi.config.mapper.ProductMapper;
import com.ronijr.algafoodapi.domain.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ProductAssembler {
    private final ProductMapper mapper;

    public ProductModel.Output toOutput(Product product) {
        return mapper.entityToOutput(product);
    }

    public ProductModel.Input toInput(Product product) {
        return mapper.entityToInput(product);
    }

    public List<ProductModel.Output> toCollectionModel(Collection<Product> products) {
        return products.stream().
                map(this::toOutput).
                collect(Collectors.toList());
    }
}