package com.ronijr.algafoodapi.domain.service.command;

import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.Product;
import com.ronijr.algafoodapi.domain.model.ProductPhoto;
import com.ronijr.algafoodapi.domain.repository.ProductRepository;
import com.ronijr.algafoodapi.domain.service.PhotoStorageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.InputStream;

import static com.ronijr.algafoodapi.domain.service.PhotoStorageService.Photo;

@Service
@Transactional
@AllArgsConstructor
public class ProductPhotoCatalogCommand {
    private final ProductRepository productRepository;
    private final AppMessageSource messageSource;
    private final PhotoStorageService storageService;

    /**
     * The entity is saved before the file so if there is a persistence problem the file will not be stored.
     * And if the file is not stored, the exception thrown in the storage service will trigger the transaction rollback.
     */
    public ProductPhoto save(Long productId, Long restaurantId, ProductPhoto photo, InputStream photoStream) {
        Product product = findProductById(productId, restaurantId);
        String oldFileName = productRepository.findPhotoByIdAndRestaurantId(productId, restaurantId).
                map(ProductPhoto::getFileName).orElse(null);

        photo.setId(productId);
        photo.setProduct(product);
        photo.setFileName(storageService.generateFileName(photo.getFileName()));

        ProductPhoto saved = productRepository.save(photo);
        productRepository.flush();

        updatePhoto(photoStream, photo.getFileName(), oldFileName);
        return saved;
    }

    public void delete(Long productId, Long restaurantId) {
        ProductPhoto productPhoto = findPhoto(productId, restaurantId);
        final String fileName = productPhoto.getFileName();
        productRepository.delete(productPhoto);
        productRepository.flush();

        storageService.remove(fileName);
    }

    private Product findProductById(Long productId, Long restaurantId) {
        return productRepository.findByIdAndRestaurantId(productId, restaurantId).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("product.restaurant.not.found", productId, productId)));
    }

    private ProductPhoto findPhoto(Long productId, Long restaurantId) {
        if (!productRepository.existsProductWithIdAndRestaurantId(productId, restaurantId)) {
            throw new EntityNotFoundException(messageSource.getMessage("product.restaurant.not.found", productId, productId));
        }
        return productRepository.findPhotoByIdAndRestaurantId(productId, restaurantId).orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage("product.photo.not.found", productId, productId)));
    }

    private void updatePhoto(InputStream inputStream, String name, String oldFileName) {
        Photo photo = Photo.builder().
                inputStream(inputStream).
                name(name).
                build();
        storageService.replace(oldFileName, photo);
    }
}