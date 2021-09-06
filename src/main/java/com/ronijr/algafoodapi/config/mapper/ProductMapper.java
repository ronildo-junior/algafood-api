package com.ronijr.algafoodapi.config.mapper;

import com.ronijr.algafoodapi.api.model.ProductModel;
import com.ronijr.algafoodapi.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product inputToEntity(ProductModel.Input input);
    ProductModel.Input entityToInput(Product product);
    ProductModel.Output entityToOutput(Product product);
    void mergeIntoTarget(ProductModel.Input input, @MappingTarget Product product);
}