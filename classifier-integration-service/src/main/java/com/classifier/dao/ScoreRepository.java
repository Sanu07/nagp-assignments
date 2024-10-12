package com.classifier.dao;

import com.classifier.entity.Interview;
import com.classifier.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {

}
