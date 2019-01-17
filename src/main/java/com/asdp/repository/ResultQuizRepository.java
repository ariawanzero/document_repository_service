package com.asdp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.asdp.entity.ResultQuizEntity;

public interface ResultQuizRepository  extends JpaSpecificationExecutor<ResultQuizEntity>, JpaRepository<ResultQuizEntity, String> {
	ResultQuizEntity findByUsernameAndQuiz(String username, String quiz);
	List<ResultQuizEntity> findByQuiz(String quiz);
}
