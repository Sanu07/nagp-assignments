package com.classifier.dao;

import com.classifier.entity.InterviewResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewResultRepository extends JpaRepository<InterviewResult, Long> {

    List<InterviewResult> findByNameInIgnoreCase(List<String> candidateNames);
}
