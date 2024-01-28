package com.gotcha.server.external.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import io.micrometer.common.lang.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {
    private final String bucket;
    private final AmazonS3 amazonS3;

    public S3Service(final AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") final String bucket) {
        this.bucket = bucket;
        this.amazonS3 = amazonS3;
    }

    public String saveUploadFile(@Nullable MultipartFile multipartFile) throws IOException {
        if (multipartFile != null) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getSize());

            String originalFilename = multipartFile.getOriginalFilename();
            int index = Objects.requireNonNull(originalFilename).lastIndexOf(".");
            String ext = originalFilename.substring(index + 1);

            String storeFileName = UUID.randomUUID() + "." + ext;
            String key = "upload/" + storeFileName;

            try (InputStream inputStream = multipartFile.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            }

            return amazonS3.getUrl(bucket, key).toString();
        } else {
            return null;
        }
    }
}
