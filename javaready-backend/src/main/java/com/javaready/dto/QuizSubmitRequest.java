package com.javaready.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmitRequest {

    private Long userId;

    private Long categoryId;

    @NotEmpty(message = "Answers list cannot be empty")
    @Valid
    private List<UserAnswerRequest> answers;
}
