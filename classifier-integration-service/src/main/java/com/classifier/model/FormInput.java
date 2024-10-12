package com.classifier.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormInput {

    private MultipartFile docFile;
    private MultipartFile excelFile;
    private Integer yearsOfExperience;
    private String genus;
}
