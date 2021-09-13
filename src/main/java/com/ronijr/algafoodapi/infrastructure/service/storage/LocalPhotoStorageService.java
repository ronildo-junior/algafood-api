package com.ronijr.algafoodapi.infrastructure.service.storage;

import com.ronijr.algafoodapi.config.storage.StorageProperties;
import com.ronijr.algafoodapi.domain.service.PhotoStorageService;
import com.ronijr.algafoodapi.infrastructure.exception.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class LocalPhotoStorageService implements PhotoStorageService {
    @Autowired
    private StorageProperties storageProperties;

    @Override
    public PhotoRetrieved retrieve(String photoName) {
        try {
            Path filePath = getFilePath(photoName);
            return PhotoRetrieved.builder().inputStream(Files.newInputStream(filePath)).build();
        } catch (IOException e) {
            throw new StorageException("Unable to retrieve file.", e);
        }
    }

    @Override
    public boolean store(Photo photo) {
        try {
            Path filePath = getFilePath(photo.getName());
            return FileCopyUtils.copy(photo.getInputStream(), Files.newOutputStream(filePath)) > 0;
        } catch (IOException e) {
            throw new StorageException("Unable to save file.", e);
        }
    }

    @Override
    public boolean remove(String photoName) {
        try {
            return Files.deleteIfExists(getFilePath(photoName));
        } catch (IOException e) {
            throw new StorageException("Unable to remove file.", e);
        }
    }

    private Path getFilePath(String fileName) {
        return storageProperties.getLocal().getPhotosPath().resolve(Path.of(fileName));
    }
}