package com.asdp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asdp.entity.SystemParameterEntity;

public interface SystemParameterRepository  extends JpaRepository<SystemParameterEntity, String> {
	List<SystemParameterEntity> findByTypeOrderByCodeAsc(String type);
}
