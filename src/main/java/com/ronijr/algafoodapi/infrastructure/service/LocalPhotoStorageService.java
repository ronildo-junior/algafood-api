package com.ronijr.algafoodapi.infrastructure.service;

import com.ronijr.algafoodapi.domain.service.PhotoStorageService;
import com.ronijr.algafoodapi.infrastructure.exception.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public final class LocalPhotoStorageService implements PhotoStorageService {
    private final Path storagePath;

    public LocalPhotoStorageService(@Value("${algafood.storage.local.photos-path}") Path storagePath) {
        this.storagePath = storagePath;
    }

    @Override
    public InputStream retrieve(String photoName) {
        try {
            Path filePath = getFilePath(photoName);
            return Files.newInputStream(filePath);
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
        return storagePath.resolve(Path.of(fileName));
    }
}