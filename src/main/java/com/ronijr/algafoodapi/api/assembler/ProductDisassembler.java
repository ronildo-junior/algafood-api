package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.ProductModel;
import com.ronijr.algafoodapi.config.mapper.ProductMapper;
import com.ronijr.algafoodapi.domain.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductDisassembler {
    private final ProductMapper mapper;

    public Product toDomain(ProductModel.Input input) {
        return mapper.inputToEntity(input);
    }

    public void copyToDomainObject(ProductModel.Input input, Product product) {
        mapper.mergeIntoTarget(input, product);
    }
}