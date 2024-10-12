package com.classifier.dao;

import com.classifier.entity.Interview;
import com.classifier.entity.InterviewResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

}
