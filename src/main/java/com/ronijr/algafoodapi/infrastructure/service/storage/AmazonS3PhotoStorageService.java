package com.ronijr.algafoodapi.infrastructure.service.storage;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ronijr.algafoodapi.config.storage.StorageProperties;
import com.ronijr.algafoodapi.domain.service.PhotoStorageService;
import com.ronijr.algafoodapi.infrastructure.exception.StorageException;
import org.springframework.beans.factory.annotation.Autowired;

public final class AmazonS3PhotoStorageService implements PhotoStorageService {
    @Autowired
    private StorageProperties storageProperties;
    @Autowired
    private AmazonS3 amazonS3;

    @Override
    public PhotoRetrieved retrieve(String photoName) {
        try {
            return PhotoRetrieved.builder().
                    url(amazonS3.getUrl(storageProperties.getS3().getBucket(), getFilePath(photoName)).toString()).
                    build();
        } catch (Exception e) {
            throw new StorageException("Unable to retrieve file.", e);
        }
    }

    @Override
    public boolean store(Photo photo) {
        try {
            var objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(photo.getContentType());
            objectMetadata.setContentLength(photo.getSize());

            var putObjectRequest = new PutObjectRequest(storageProperties.getS3().getBucket(),
                    getFilePath(photo.getName()),
                    photo.getInputStream(),
                    objectMetadata).
                    withCannedAcl(CannedAccessControlList.PublicRead);

            amazonS3.putObject(putObjectRequest);
            return true;
        } catch (SdkClientException e) {
            throw new StorageException("Unable to store file.", e);
        }
    }

    @Override
    public boolean remove(String photoName) {
        try {
            var deleteObjectRequest = new DeleteObjectRequest(storageProperties.getS3().getBucket(), getFilePath(photoName));
            amazonS3.deleteObject(deleteObjectRequest);
            return true;
        } catch (Exception e) {
            throw new StorageException("Unable to remove file.", e);
        }
    }

    private String getFilePath(String fileName) {
        return storageProperties.getS3().getPhotosDirectory() + "/" + fileName;
    }
}