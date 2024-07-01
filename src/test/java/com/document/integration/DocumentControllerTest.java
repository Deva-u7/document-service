package com.document.integration;

import com.document.controller.DocumentController;
import com.document.dto.DocumentDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import static com.document.utils.DocumentUtils.getExpectedResponse;
import static com.document.utils.DocumentUtils.getMockMultipartFile;
import static com.document.utils.DocumentUtils.getRequest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
public class DocumentControllerTest {
    private WireMockServer wireMockServer;

    @Autowired
    private DocumentController documentController;

    @Autowired
    private S3Client amazonS3;

    private MockMvc mockMvc;

    @Value("${spring.s3.bucket}")
    String bucketName;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(documentController).build();
        amazonS3.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
    }

    @AfterEach
    void tearDown() {
        ListObjectsV2Response listResponse = amazonS3.listObjectsV2(ListObjectsV2Request.builder().bucket(bucketName).build());
        for (S3Object s3Object : listResponse.contents()) {
            amazonS3.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(s3Object.key()).build());
        }
        amazonS3.deleteBucket(DeleteBucketRequest.builder().bucket(bucketName).build());
    }

    @Test
    void testFileUpload() throws Exception {
        DocumentDTO request = getRequest();
        MockMultipartFile multipartFile = getMockMultipartFile("test.txt");

        String responseString = mockMvc.perform(MockMvcRequestBuilders
                .multipart("/api/v1/document")
                .file(multipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();
        DocumentDTO actualResponse = objectMapper.readValue(responseString, DocumentDTO.class);
        Assertions.assertEquals(getExpectedResponse(), actualResponse);
    }

}
    