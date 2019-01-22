package com.asdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.asdp.entity.DocumentEntity;
import com.asdp.request.DocumentRequest;
import com.asdp.service.DocumentService;
import com.asdp.util.SystemRestConstant;

@RestController
@RequestMapping(SystemRestConstant.DocumentConstant.DOCUMENT_CONTROLLER)
public class DocumentController {
	
	@Autowired
	private DocumentService documentService;
	
	@PostMapping(SystemRestConstant.DocumentConstant.UPLOAD_DOCUMENT_ADDR)
	public String saveDocumentFile(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) throws Exception {
		return documentService.saveDocumentFile(file, id);
	}
	
	@PostMapping(SystemRestConstant.DocumentConstant.SAVE_DOCUMENT_ADDR)
	public String saveDocument(@RequestBody DocumentEntity request) throws Exception {				
		return documentService.saveDocument(request);
	}

	@PostMapping(SystemRestConstant.DocumentConstant.FIND_DOCUMENT_DETAIL_ADDR)
	public String findDocumentDetail(@RequestBody DocumentEntity request) throws Exception {
		return documentService.findDocumentDetail(request.getId());
	}
	
	@PostMapping(SystemRestConstant.DocumentConstant.SEARCH_DOCUMENT_ADDR)
	public String searchDocument(@RequestBody DocumentRequest request) throws Exception {
		return documentService.searchDocument(request);
	}
	
	@PostMapping(SystemRestConstant.DocumentConstant.SEARCH_DOCUMENT_PENDING_ADDR)
	public String searchDocumentPending(@RequestBody DocumentRequest request) throws Exception {
		return documentService.searchDocumentPending(request);
	}
	
	@PostMapping(SystemRestConstant.DocumentConstant.APPROVE_DOCUMENT_ADDR)
	public String approveDocument(@RequestBody DocumentEntity request) throws Exception {
		return documentService.approveDocument(request.getId());
	}
	
	@PostMapping(SystemRestConstant.DocumentConstant.REJECTED_DOCUMENT_ADDR)
	public String rejectedDocument(@RequestBody DocumentEntity request) throws Exception {
		return documentService.rejectedDocument(request);
	}
	
	@PostMapping(SystemRestConstant.DocumentConstant.SEARCH_DOCUMENT_HISTORY_ADDR)
	public String searchDocumentHistory(@RequestBody DocumentRequest request) throws Exception {
		return documentService.searchDocumentHistory(request);
	}
	
	@PostMapping(SystemRestConstant.DocumentConstant.SEARCH_DETAIL_DOCUMENT_HISTORY_ADDR)
	public String searchDetailDocumentHistory(@RequestBody DocumentRequest request) throws Exception {
		return documentService.searchDetailDocumentHistory(request);
	}
	
	@PostMapping(SystemRestConstant.DocumentConstant.READ_DOCUMENT_DETAIL_ADDR)
	public String readDocumentDetail(@RequestBody DocumentEntity request) throws Exception {
		return documentService.readDocumentDetail(request.getId());
	}
	
	@PostMapping(SystemRestConstant.DocumentConstant.DASHBOAR_SEARCH_ADDR)
	public String dashboardSearch(@RequestBody DocumentRequest request) throws Exception {
		return documentService.searchDocumentAdvanced(request);
	}

}
