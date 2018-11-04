package com.asdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.asdp.entity.MateriQuizEntity;

public interface MateriQuizRepository extends JpaSpecificationExecutor<MateriQuizEntity>, JpaRepository<MateriQuizEntity, String> {
	MateriQuizEntity findByName(String name);
}
