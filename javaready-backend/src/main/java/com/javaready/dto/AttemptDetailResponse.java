package com.javaready.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttemptDetailResponse {

    private Long id;
    private Long userId;
    private String userName;
    private Long categoryId;
    private String categoryName;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private Integer wrongAnswers;
    private Double score;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private List<AttemptAnswerResponse> answers;
}
