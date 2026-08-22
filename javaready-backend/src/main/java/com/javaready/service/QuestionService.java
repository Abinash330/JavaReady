package com.javaready.service;

import com.javaready.dto.QuestionRequest;
import com.javaready.dto.QuestionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {
    List<QuestionResponse> getAllQuestions();
    List<QuestionResponse> getActiveQuestions();
    Page<QuestionResponse> searchQuestions(Long categoryId, String difficulty, String search, Pageable pageable);
    QuestionResponse getQuestionById(Long id);
    List<QuestionResponse> getQuestionsByCategory(Long categoryId);
    QuestionResponse createQuestion(QuestionRequest request);
    QuestionResponse updateQuestion(Long id, QuestionRequest request);
    void deleteQuestion(Long id);
}
