package com.classifier.dao;

import com.classifier.entity.Question;
import com.classifier.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
