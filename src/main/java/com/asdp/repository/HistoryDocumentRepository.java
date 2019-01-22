package com.asdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.asdp.entity.HistoryDocumentEntity;

public interface HistoryDocumentRepository  extends JpaSpecificationExecutor<HistoryDocumentEntity>, JpaRepository<HistoryDocumentEntity, String>{
	HistoryDocumentEntity findByUsernameAndDocument(String username, String Document);
	int countByDocument(String Document);
}
