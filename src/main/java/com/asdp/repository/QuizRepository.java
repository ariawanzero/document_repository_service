package com.asdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.asdp.entity.QuizEntity;

public interface QuizRepository extends JpaSpecificationExecutor<QuizEntity>, JpaRepository<QuizEntity, String>  {
	QuizEntity findByName(String name);
}
