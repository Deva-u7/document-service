package com.document.utils;

import com.document.dto.DocumentDTO;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class DocumentUtils {

    public static DocumentDTO getRequest() {
        return DocumentDTO.builder()
                .firstName("firstName")
                .lastName("lastName")
                .age(27)
                .build();
    }

    public static DocumentDTO getExpectedResponse() {
        return DocumentDTO.builder()
                .id(1l)
                .firstName("firstName")
                .lastName("lastName")
                .age(27)
                .fileURL("https://s3.amazonaws.com/test/object/test.txt")
                .build();
    }

    public static MockMultipartFile getMockMultipartFile(String fileName) {
        return new MockMultipartFile(
                "file",
                fileName,
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "Hello, World!".getBytes());
    }
}
