package com.test_auth.aws.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Objects;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(
            @Value("${cloud.aws.credentials.access-key}") String accessKey,
            @Value("${cloud.aws.credentials.secret-key}") String secretKey,
            @Value("${cloud.aws.region.static}") String region,
            @Value("${cloud.aws.s3.bucket}") String bucketName
    ) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.of(region))
                .build();
        this.bucketName = bucketName;
    }

    private boolean isImage(String contentType) {
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/webp") ||
                contentType.equals("image/jpg");
    }

    public ResponseEntity<String> uploadFile(MultipartFile file) throws IOException {
        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("The file is empty");
            }

            // Verificar si el archivo es una imagen
            String contentType = file.getContentType();
            if (contentType == null || !isImage(contentType)) {
                return new ResponseEntity<>("Only image files are allowed", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }

            // Generar un nombre único para el archivo
            String fileName = System.currentTimeMillis() + "_" +
                    Objects.requireNonNull(file.getOriginalFilename()).replaceAll(" ", "_");

            // Subir el archivo a S3
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );

            // Construir la URL del archivo subido
            StringBuilder urlImageInAws = new StringBuilder();
            urlImageInAws.append("https://").append(bucketName).append(".s3.amazonaws.com/").append(fileName);
            System.out.println("Url Image: " + urlImageInAws.toString());

            return new ResponseEntity<>(urlImageInAws.toString(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}