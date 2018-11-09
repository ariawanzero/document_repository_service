package com.asdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asdp.entity.ResultQuizEntity;

public interface ResultQuizRepository  extends JpaRepository<ResultQuizEntity, String> {
	ResultQuizEntity findByUsernameAndQuiz(String username, String quiz);
}
