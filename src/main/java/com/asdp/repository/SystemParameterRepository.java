package com.asdp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.asdp.entity.SystemParameterEntity;

public interface SystemParameterRepository  extends JpaSpecificationExecutor<SystemParameterEntity>, JpaRepository<SystemParameterEntity, String> {
	List<SystemParameterEntity> findByTypeOrderByCodeAsc(String type);
	SystemParameterEntity findByCode(String code);
}
