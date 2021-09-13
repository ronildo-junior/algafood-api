package com.ronijr.algafoodapi.config.storage;

import com.amazonaws.regions.Regions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Getter @Setter
@Component
@ConfigurationProperties("algafood.storage")
public final class StorageProperties {
    private Local local = new Local();
    private S3 s3 = new S3();
    private StorageType storageType = StorageType.LOCAL;

    public enum StorageType {
        LOCAL, S3
    }

    @Getter @Setter
    public static class Local {
        private Path photosPath;
    }

    @Getter @Setter
    public static class S3 {
        private Regions region;
        private String bucket;
        private String photosDirectory;
        private String accessKey;
        private String secretKey;
    }
}