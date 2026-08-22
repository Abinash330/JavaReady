package com.javaready.service.impl;

import com.javaready.dto.*;
import com.javaready.entity.Attempt;
import com.javaready.entity.AttemptAnswer;
import com.javaready.entity.Category;
import com.javaready.entity.Question;
import com.javaready.entity.User;
import com.javaready.exception.ResourceNotFoundException;
import com.javaready.repository.AttemptAnswerRepository;
import com.javaready.repository.AttemptRepository;
import com.javaready.repository.CategoryRepository;
import com.javaready.repository.QuestionRepository;
import com.javaready.repository.UserRepository;
import com.javaready.service.AttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttemptServiceImpl implements AttemptService {

    private final AttemptRepository attemptRepository;
    private final AttemptAnswerRepository attemptAnswerRepository;
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AttemptDetailResponse submitQuiz(QuizSubmitRequest request) {
        User user = null;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));
        }

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
        }

        int totalQuestions = request.getAnswers().size();
        int correctCount = 0;
        int wrongCount = 0;

        Attempt attempt = Attempt.builder()
                .user(user)
                .category(category)
                .totalQuestions(totalQuestions)
                .status("COMPLETED")
                .startedAt(LocalDateTime.now())
                .completedAt(LocalDateTime.now())
                .build();

        List<AttemptAnswer> attemptAnswers = new ArrayList<>();

        for (UserAnswerRequest answerReq : request.getAnswers()) {
            Question question = questionRepository.findById(answerReq.getQuestionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + answerReq.getQuestionId()));

            String selected = answerReq.getSelectedAnswer() != null ? answerReq.getSelectedAnswer().trim() : "";
            
            // By default, if the user provides an answer that is non-empty, we record it
            boolean isCorrect = false;
            String expected = question.getExplanation();

            if (!selected.isEmpty()) {
                // In interview prep question evaluation, we mark answer as attempted
                isCorrect = true;
                correctCount++;
            } else {
                wrongCount++;
            }

            AttemptAnswer attemptAnswer = AttemptAnswer.builder()
                    .attempt(attempt)
                    .question(question)
                    .selectedAnswer(selected)
                    .correctAnswer(expected)
                    .isCorrect(isCorrect)
                    .build();

            attemptAnswers.add(attemptAnswer);
        }

        double scorePercentage = totalQuestions > 0 ? ((double) correctCount / totalQuestions) * 100.0 : 0.0;

        attempt.setCorrectAnswers(correctCount);
        attempt.setWrongAnswers(wrongCount);
        attempt.setScore(Math.round(scorePercentage * 100.0) / 100.0);
        attempt.setAnswers(attemptAnswers);

        Attempt savedAttempt = attemptRepository.save(attempt);

        return mapToDetailResponse(savedAttempt);
    }

    @Override
    @Transactional(readOnly = true)
    public AttemptDetailResponse getAttemptById(Long id) {
        Attempt attempt = attemptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attempt not found with id: " + id));
        return mapToDetailResponse(attempt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttemptResponse> getUserAttempts(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return attemptRepository.findByUserIdOrderByStartedAtDesc(userId)
                .stream()
                .map(this::mapToSummaryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttemptResponse> getAllAttempts() {
        return attemptRepository.findAll()
                .stream()
                .map(this::mapToSummaryResponse)
                .toList();
    }

    private AttemptResponse mapToSummaryResponse(Attempt attempt) {
        return AttemptResponse.builder()
                .id(attempt.getId())
                .userId(attempt.getUser() != null ? attempt.getUser().getId() : null)
                .userName(attempt.getUser() != null ? attempt.getUser().getName() : "Anonymous")
                .categoryId(attempt.getCategory() != null ? attempt.getCategory().getId() : null)
                .categoryName(attempt.getCategory() != null ? attempt.getCategory().getName() : null)
                .totalQuestions(attempt.getTotalQuestions())
                .correctAnswers(attempt.getCorrectAnswers())
                .wrongAnswers(attempt.getWrongAnswers())
                .score(attempt.getScore())
                .status(attempt.getStatus())
                .startedAt(attempt.getStartedAt())
                .completedAt(attempt.getCompletedAt())
                .build();
    }

    private AttemptDetailResponse mapToDetailResponse(Attempt attempt) {
        List<AttemptAnswerResponse> answerResponses = attempt.getAnswers().stream()
                .map(a -> AttemptAnswerResponse.builder()
                        .id(a.getId())
                        .questionId(a.getQuestion().getId())
                        .questionTitle(a.getQuestion().getTitle())
                        .questionText(a.getQuestion().getQuestionText())
                        .selectedAnswer(a.getSelectedAnswer())
                        .correctAnswer(a.getCorrectAnswer())
                        .isCorrect(a.getIsCorrect())
                        .explanation(a.getQuestion().getExplanation())
                        .build())
                .toList();

        return AttemptDetailResponse.builder()
                .id(attempt.getId())
                .userId(attempt.getUser() != null ? attempt.getUser().getId() : null)
                .userName(attempt.getUser() != null ? attempt.getUser().getName() : "Anonymous")
                .categoryId(attempt.getCategory() != null ? attempt.getCategory().getId() : null)
                .categoryName(attempt.getCategory() != null ? attempt.getCategory().getName() : null)
                .totalQuestions(attempt.getTotalQuestions())
                .correctAnswers(attempt.getCorrectAnswers())
                .wrongAnswers(attempt.getWrongAnswers())
                .score(attempt.getScore())
                .status(attempt.getStatus())
                .startedAt(attempt.getStartedAt())
                .completedAt(attempt.getCompletedAt())
                .answers(answerResponses)
                .build();
    }
}
