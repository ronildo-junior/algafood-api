package com.ronijr.algafoodapi.config.storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.ronijr.algafoodapi.domain.service.PhotoStorageService;
import com.ronijr.algafoodapi.infrastructure.service.storage.AmazonS3PhotoStorageService;
import com.ronijr.algafoodapi.infrastructure.service.storage.LocalPhotoStorageService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.ronijr.algafoodapi.config.storage.StorageProperties.StorageType;

@Configuration
@AllArgsConstructor
public class StorageConfig {
    private final StorageProperties storageProperties;

    @Bean
    @ConditionalOnProperty(name = "algafood.storage.storage-type", havingValue = "s3")
    public AmazonS3 amazonS3() {
        var credentials = new BasicAWSCredentials(
                storageProperties.getS3().getAccessKey(),
                storageProperties.getS3().getSecretKey());
        return AmazonS3ClientBuilder.standard().
                withRegion(storageProperties.getS3().getRegion()).
                withCredentials(new AWSStaticCredentialsProvider(credentials)).
                build();
    }

    @Bean
    public PhotoStorageService photoStorageService() {
        if (StorageType.S3.equals(storageProperties.getStorageType())) {
            return new AmazonS3PhotoStorageService();
        } else {
            return new LocalPhotoStorageService();
        }
    }
}