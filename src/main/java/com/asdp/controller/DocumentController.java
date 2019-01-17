package com.asdp.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asdp.entity.DocumentEntity;
import com.asdp.request.DocumentRequest;
import com.asdp.request.QuizSearchRequest;
import com.asdp.util.SystemRestConstant;

@RestController
@RequestMapping(SystemRestConstant.DocumentConstant.DOCUMENT_CONTROLLER)
public class DocumentController {
	/*@Autowired
	private QuizService quizService;*/
	
	@PostMapping(SystemRestConstant.DocumentConstant.SAVE_DOCUMENT_ADDR)
	public String saveDocument(@RequestBody DocumentEntity request) throws Exception {				
		return "";
	}

	@PostMapping(SystemRestConstant.DocumentConstant.FIND_DOCUMENT_DETAIL_ADDR)
	public String findDocumentDetail(@RequestBody DocumentEntity request) throws Exception {
		return "";
	}
	
	@PostMapping(SystemRestConstant.DocumentConstant.SEARCH_DOCUMENT_ADDR)
	public String searchDocument(@RequestBody DocumentRequest request) throws Exception {
		return "";
	}
	
	@PostMapping(SystemRestConstant.DocumentConstant.SEARCH_DOCUMENT_PENDING_ADDR)
	public String searchDocumentPending(@RequestBody DocumentRequest request) throws Exception {
		return "";
	}
	
	@PostMapping(SystemRestConstant.DocumentConstant.APPROVE_DOCUMENT_ADDR)
	public String approveDocument(@RequestBody QuizSearchRequest request) throws Exception {
		return "";
	}
	
	@PostMapping(SystemRestConstant.DocumentConstant.FIND_HISTORY_DOCUMENT_DETAIL_ADDR)
	public String findHistoryDocumentDetail(@RequestBody DocumentRequest request) throws Exception {
		return "";
	}

}
