package com.ronijr.algafoodapi.domain.service;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.UUID;

public interface PhotoStorageService {
    PhotoRetrieved retrieve(String photoName);
    boolean store(Photo photo);
    boolean remove(String photoName);

    default boolean replace(String oldFileName, Photo newPhoto) {
        this.store(newPhoto);
        if (StringUtils.hasLength(oldFileName)) {
            remove(oldFileName);
        }
        return true;
    }

    default String generateFileName(String fileName) {
        return UUID.randomUUID().toString() + "_" + fileName;
    }

    @Getter
    @Builder
    class Photo {
        private String name;
        private String contentType;
        private InputStream inputStream;
        private Long size;
    }

    @Getter
    @Builder
    class PhotoRetrieved {
        private InputStream inputStream;
        private String url;

        public boolean hasInputStream() {
            return this.inputStream != null;
        }

        public boolean hasUrl() {
            return StringUtils.hasLength(this.url);
        }
    }
}