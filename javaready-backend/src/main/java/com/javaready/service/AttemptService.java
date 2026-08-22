package com.javaready.service;

import com.javaready.dto.AttemptDetailResponse;
import com.javaready.dto.AttemptResponse;
import com.javaready.dto.QuizSubmitRequest;

import java.util.List;

public interface AttemptService {

    AttemptDetailResponse submitQuiz(QuizSubmitRequest request);

    AttemptDetailResponse getAttemptById(Long id);

    List<AttemptResponse> getUserAttempts(Long userId);

    List<AttemptResponse> getAllAttempts();
}
