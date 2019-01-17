package com.asdp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.asdp.entity.QuestionEntity;
import com.asdp.entity.QuizEntity;

public interface QuestionRepository extends JpaSpecificationExecutor<QuestionEntity>, JpaRepository<QuestionEntity, String> {
	List<QuestionEntity> findByQuiz(QuizEntity quiz);
}
