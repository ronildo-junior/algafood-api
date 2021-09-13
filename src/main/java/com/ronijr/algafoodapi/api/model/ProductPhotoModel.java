package com.ronijr.algafoodapi.api.model;

import com.ronijr.algafoodapi.config.validation.FileContentType;
import com.ronijr.algafoodapi.config.validation.FileSize;
import lombok.Value;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public final class ProductPhotoModel {
    private ProductPhotoModel() {}

    interface Photo {
        @NotNull @FileSize(max = "5MB")
        @FileContentType(allowed = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
        MultipartFile getPhoto(); }
    interface Description { @NotBlank String getDescription(); }

    @Value
    public static class Input implements Description, Photo {
        MultipartFile photo;
        String description;
    }

    @Value
    public static class Output {
        Long id;
        String fileName;
        String description;
        String contentType;
        Long size;
    }
}