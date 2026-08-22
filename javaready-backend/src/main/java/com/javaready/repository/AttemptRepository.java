package com.javaready.repository;

import com.javaready.entity.Attempt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttemptRepository extends JpaRepository<Attempt, Long> {

    List<Attempt> findByUserIdOrderByStartedAtDesc(Long userId);

    Page<Attempt> findByUserId(Long userId, Pageable pageable);

    List<Attempt> findByCategoryId(Long categoryId);
}
