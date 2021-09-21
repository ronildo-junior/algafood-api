package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.ProductPhotoAssembler;
import com.ronijr.algafoodapi.api.model.ProductPhotoModel;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.ProductPhoto;
import com.ronijr.algafoodapi.domain.service.PhotoStorageService;
import com.ronijr.algafoodapi.domain.service.command.ProductPhotoCatalogCommand;
import com.ronijr.algafoodapi.domain.service.query.ProductPhotoCatalogQuery;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/restaurants/{restaurantId}/products/{productId}/photo", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class RestaurantProductPhotoController {
    private final ProductPhotoCatalogCommand photoCommand;
    private final ProductPhotoCatalogQuery photoQuery;
    private final ProductPhotoAssembler photoAssembler;
    private final PhotoStorageService storageService;

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductPhotoModel.Output> updateProductPhoto(
            @PathVariable Long restaurantId, @PathVariable Long productId, @Valid ProductPhotoModel.Input photoModel) throws IOException {
        MultipartFile photoFile = photoModel.getPhoto();
        ProductPhoto productPhoto = ProductPhoto.builder().
                contentType(photoFile.getContentType()).
                description(photoModel.getDescription()).
                fileName(photoFile.getOriginalFilename()).
                size(photoFile.getSize()).
                build();
        ProductPhoto saved = photoCommand.save(productId, restaurantId, productPhoto, photoFile.getInputStream());
        return ResponseEntity.ok(photoAssembler.toOutput(saved));
    }

    @GetMapping
    public ResponseEntity<ProductPhotoModel.Output> getInfo(@PathVariable Long restaurantId, @PathVariable Long productId) {
        return ResponseEntity.ok(photoAssembler.toOutput(photoQuery.findByIdAndRestaurantId(productId, restaurantId)));
    }

    @GetMapping(produces = MediaType.ALL_VALUE)
    public ResponseEntity<Object> getFile(
            @PathVariable Long restaurantId, @PathVariable Long productId, @RequestHeader("accept") String acceptHeader) throws HttpMediaTypeNotAcceptableException {
        try {
            ProductPhoto productPhoto = photoQuery.findByIdAndRestaurantId(productId, restaurantId);
            MediaType photoType = MediaType.parseMediaType(productPhoto.getContentType());
            List<MediaType> mediaTypes = MediaType.parseMediaTypes(acceptHeader);

            verifyCompatibility(photoType, mediaTypes);

            PhotoStorageService.PhotoRetrieved photoRetrieved = storageService.retrieve(productPhoto.getFileName());

            if (photoRetrieved.hasInputStream()) {
                return ResponseEntity.ok().
                        contentType(photoType).
                        body(new InputStreamResource(photoRetrieved.getInputStream()));
            } else {
                return ResponseEntity.status(HttpStatus.FOUND).
                        header(HttpHeaders.LOCATION, photoRetrieved.getUrl()).
                        build();
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long restaurantId, @PathVariable Long productId) {
        photoCommand.delete(productId, restaurantId);
    }

    private void verifyCompatibility(MediaType mediaType, List<MediaType> mediaTypes) throws HttpMediaTypeNotAcceptableException {
        if (mediaTypes.stream().noneMatch(r -> r.isCompatibleWith(mediaType))) {
            throw new HttpMediaTypeNotAcceptableException(mediaTypes);
        }
    }
}