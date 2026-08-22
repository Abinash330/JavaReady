package com.javaready.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "attempt_answers",
        indexes = {
                @Index(name = "idx_attempt_answer_attempt", columnList = "attempt_id"),
                @Index(name = "idx_attempt_answer_question", columnList = "question_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttemptAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "attempt_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_attempt_answer_attempt")
    )
    private Attempt attempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "question_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_attempt_answer_question")
    )
    private Question question;

    @Column(name = "selected_answer", columnDefinition = "TEXT")
    private String selectedAnswer;

    @Column(name = "correct_answer", columnDefinition = "TEXT")
    private String correctAnswer;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;
}
