package com.javaready.controller;

import com.javaready.dto.AttemptDetailResponse;
import com.javaready.dto.AttemptResponse;
import com.javaready.dto.QuizSubmitRequest;
import com.javaready.service.AttemptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attempts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AttemptController {

    private final AttemptService attemptService;

    @PostMapping
    public ResponseEntity<AttemptDetailResponse> submitQuiz(@Valid @RequestBody QuizSubmitRequest request) {
        AttemptDetailResponse response = attemptService.submitQuiz(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttemptDetailResponse> getAttemptById(@PathVariable Long id) {
        return ResponseEntity.ok(attemptService.getAttemptById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AttemptResponse>> getUserAttempts(@PathVariable Long userId) {
        return ResponseEntity.ok(attemptService.getUserAttempts(userId));
    }

    @GetMapping
    public ResponseEntity<List<AttemptResponse>> getAllAttempts() {
        return ResponseEntity.ok(attemptService.getAllAttempts());
    }
}
