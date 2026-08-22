package com.javaready.service.impl;

import com.javaready.dto.QuestionRequest;
import com.javaready.dto.QuestionResponse;
import com.javaready.entity.Category;
import com.javaready.entity.Question;
import com.javaready.entity.User;
import com.javaready.exception.ResourceNotFoundException;
import com.javaready.repository.CategoryRepository;
import com.javaready.repository.QuestionRepository;
import com.javaready.repository.UserRepository;
import com.javaready.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> getAllQuestions() {
        return questionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> getActiveQuestions() {
        return questionRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionResponse> searchQuestions(Long categoryId, String difficulty, String search, Pageable pageable) {
        return questionRepository.searchQuestions(categoryId, difficulty, search, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponse getQuestionById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));
        return mapToResponse(question);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuestionsByCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        return questionRepository.findByCategoryIdAndActiveTrue(categoryId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public QuestionResponse createQuestion(QuestionRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        User createdBy = null;
        if (request.getCreatedById() != null) {
            createdBy = userRepository.findById(request.getCreatedById())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getCreatedById()));
        }

        Question question = Question.builder()
                .title(request.getTitle())
                .questionText(request.getQuestionText())
                .explanation(request.getExplanation())
                .code(request.getCode())
                .difficulty(request.getDifficulty() != null ? request.getDifficulty() : "EASY")
                .tags(request.getTags())
                .active(request.getActive() != null ? request.getActive() : true)
                .category(category)
                .createdBy(createdBy)
                .build();

        Question saved = questionRepository.save(question);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public QuestionResponse updateQuestion(Long id, QuestionRequest request) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        User createdBy = question.getCreatedBy();
        if (request.getCreatedById() != null) {
            createdBy = userRepository.findById(request.getCreatedById())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getCreatedById()));
        }

        question.setTitle(request.getTitle());
        question.setQuestionText(request.getQuestionText());
        question.setExplanation(request.getExplanation());
        question.setCode(request.getCode());
        question.setDifficulty(request.getDifficulty());
        question.setTags(request.getTags());
        if (request.getActive() != null) {
            question.setActive(request.getActive());
        }
        question.setCategory(category);
        question.setCreatedBy(createdBy);

        Question updated = questionRepository.save(question);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question not found with id: " + id);
        }
        questionRepository.deleteById(id);
    }

    private QuestionResponse mapToResponse(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .title(question.getTitle())
                .questionText(question.getQuestionText())
                .explanation(question.getExplanation())
                .code(question.getCode())
                .difficulty(question.getDifficulty())
                .tags(question.getTags())
                .active(question.getActive())
                .categoryId(question.getCategory() != null ? question.getCategory().getId() : null)
                .categoryName(question.getCategory() != null ? question.getCategory().getName() : null)
                .createdById(question.getCreatedBy() != null ? question.getCreatedBy().getId() : null)
                .createdByName(question.getCreatedBy() != null ? question.getCreatedBy().getName() : null)
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .build();
    }
}
