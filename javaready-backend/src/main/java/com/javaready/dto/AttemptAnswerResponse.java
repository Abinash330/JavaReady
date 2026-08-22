package com.javaready.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttemptAnswerResponse {

    private Long id;
    private Long questionId;
    private String questionTitle;
    private String questionText;
    private String selectedAnswer;
    private String correctAnswer;
    private Boolean isCorrect;
    private String explanation;
}
