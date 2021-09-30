package com.ronijr.algafoodapi.config.mapper;

import com.ronijr.algafoodapi.api.v1.model.ProductPhotoModel;
import com.ronijr.algafoodapi.domain.model.ProductPhoto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductPhotoMapper {
    ProductPhotoModel.Output entityToOutput(ProductPhoto product);
}