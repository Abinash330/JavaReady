package com.javaready.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {

    private Long id;
    private String title;
    private String questionText;
    private String explanation;
    private String code;
    private String difficulty;
    private String tags;
    private Boolean active;
    private Long categoryId;
    private String categoryName;
    private Long createdById;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
