package com.asdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.asdp.entity.HistoryLoginEntity;

public interface HistoryLoginRepository extends JpaSpecificationExecutor<HistoryLoginEntity>, JpaRepository<HistoryLoginEntity, String>{

}
