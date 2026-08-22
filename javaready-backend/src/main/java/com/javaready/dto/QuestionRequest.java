package com.javaready.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {

    @NotBlank(message = "Title is mandatory")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "Question text is mandatory")
    private String questionText;

    private String explanation;

    private String code;

    @Size(max = 50, message = "Difficulty cannot exceed 50 characters")
    private String difficulty;

    @Size(max = 500, message = "Tags cannot exceed 500 characters")
    private String tags;

    @Builder.Default
    private Boolean active = true;

    @NotNull(message = "Category ID is mandatory")
    private Long categoryId;

    private Long createdById;
}
