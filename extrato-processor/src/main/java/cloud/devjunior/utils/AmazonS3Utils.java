package cloud.devjunior.utils;

import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;

public abstract class AmazonS3Utils {

    @ConfigProperty(name = "bucket.name")
    String bucketName;

    @Inject
    protected S3Client s3Client;

    protected GetObjectRequest buildGetRequest(String key) {
        return GetObjectRequest.builder().bucket(bucketName).key(key).build();
    }

    protected ListObjectsRequest buildListRequest() {
        return ListObjectsRequest.builder().bucket(bucketName).build();
    }
}
