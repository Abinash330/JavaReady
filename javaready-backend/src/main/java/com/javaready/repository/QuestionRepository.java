package com.javaready.repository;

import com.javaready.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByCategoryId(Long categoryId);

    List<Question> findByCategoryIdAndActiveTrue(Long categoryId);

    List<Question> findByDifficulty(String difficulty);

    List<Question> findByActiveTrue();

    Page<Question> findByActiveTrue(Pageable pageable);

    @Query("SELECT q FROM Question q WHERE q.active = true AND " +
           "(:categoryId IS NULL OR q.category.id = :categoryId) AND " +
           "(:difficulty IS NULL OR LOWER(q.difficulty) = LOWER(:difficulty)) AND " +
           "(:search IS NULL OR LOWER(q.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(q.questionText) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(COALESCE(q.tags, '')) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Question> searchQuestions(
            @Param("categoryId") Long categoryId,
            @Param("difficulty") String difficulty,
            @Param("search") String search,
            Pageable pageable
    );
}
