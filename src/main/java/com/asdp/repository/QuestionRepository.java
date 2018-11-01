package com.asdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asdp.entity.QuestionEntity;

public interface QuestionRepository extends JpaRepository<QuestionEntity, String> {

}
