package com.document.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class AWSS3FileUploadService {

    @Autowired
    private S3Client amazonS3;

    @Value("${spring.s3.bucket}")
    String bucketName;
    private static final Logger log = LoggerFactory.getLogger(AWSS3FileUploadService.class);

    public String fileUpload(MultipartFile multipartFile){
        String resourceUrl = null;
        String path = "/object";
        String fileName = multipartFile.getOriginalFilename();
        try {
            final File file = convertMultiPartFileToFile(multipartFile);
            uploadResourceToS3Bucket(bucketName, file, String.format("/%s/%s",path, fileName));
            file.delete();
            resourceUrl = generateResourceUrl(bucketName, path, fileName);
        } catch (final AwsServiceException ex) {
            log.error("Error while upload document in aws :{}",ex.awsErrorDetails());
        }
        return resourceUrl;
    }

    private String generateResourceUrl(String bucketName, String path, String fileName) {
        String resourceUrl;
        resourceUrl = "https://s3.amazonaws.com/" + bucketName + path + "/" + fileName;
        return resourceUrl;
    }

    private void uploadResourceToS3Bucket(String bucketName, File file, String uniqueFileNameWithPath) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(uniqueFileNameWithPath)
                .build();
        amazonS3.putObject(putObjectRequest,  RequestBody.fromFile(file));
    }

    private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
        final File file = new File("test");
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException ex) {
            log.error("Error while convert document :{}",ex.getLocalizedMessage());
        }
        return file;
    }
}
