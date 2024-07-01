package com.document.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DocumentDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private int age;
    private String fileURL;
}
