package com.asdp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asdp.entity.QuestionEntity;
import com.asdp.entity.QuizEntity;

public interface QuestionRepository extends JpaRepository<QuestionEntity, String> {
	List<QuestionEntity> findByQuiz(QuizEntity quiz);
}
