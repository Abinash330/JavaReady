package com.javaready.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswerRequest {

    @NotNull(message = "Question ID is mandatory")
    private Long questionId;

    private String selectedAnswer;
}
