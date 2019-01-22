package com.asdp.service;

import org.springframework.web.multipart.MultipartFile;

import com.asdp.entity.DocumentEntity;
import com.asdp.request.DocumentRequest;

public interface DocumentService {
	String saveDocumentFile(MultipartFile file, String id) throws Exception;
	String saveDocument(DocumentEntity request) throws Exception;
	String findDocumentDetail(String id) throws Exception;
	String readDocumentDetail(String id) throws Exception;
	String searchDocument(DocumentRequest request) throws Exception;
	String searchDocumentPending(DocumentRequest request) throws Exception;
	String searchDocumentAdvanced(DocumentRequest request) throws Exception;
	String approveDocument(String id) throws Exception;
	String rejectedDocument(DocumentEntity request) throws Exception;
	String searchDocumentHistory(DocumentRequest request) throws Exception;
	String searchDetailDocumentHistory(DocumentRequest request) throws Exception;
}
