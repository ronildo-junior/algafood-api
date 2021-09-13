package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.ProductPhotoModel;
import com.ronijr.algafoodapi.config.mapper.ProductPhotoMapper;
import com.ronijr.algafoodapi.domain.model.ProductPhoto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductPhotoAssembler {
    private final ProductPhotoMapper mapper;

    public ProductPhotoModel.Output toOutput(ProductPhoto productPhoto) {
        return mapper.entityToOutput(productPhoto);
    }
}