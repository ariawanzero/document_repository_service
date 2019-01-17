package com.asdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.asdp.entity.DocumentEntity;

public interface DocumentRepository extends JpaSpecificationExecutor<DocumentEntity>, JpaRepository<DocumentEntity, String>{

}
