package com.asdp.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.asdp.entity.HistoryLoginEntity;
import com.asdp.entity.UserEntity;

public interface HistoryLoginRepository extends JpaSpecificationExecutor<HistoryLoginEntity>, JpaRepository<HistoryLoginEntity, String>{
	HistoryLoginEntity findByUserAndDateLoginBetween(UserEntity user, Date date, Date date2) throws Exception;
}
