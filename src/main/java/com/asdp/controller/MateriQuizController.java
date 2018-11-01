package com.asdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.asdp.entity.MateriQuizEntity;
import com.asdp.request.MateriQuizSearchRequest;
import com.asdp.service.MateriQuizService;
import com.asdp.util.SystemRestConstant;

@RestController
@RequestMapping(SystemRestConstant.MateriQuizConstant.MATERI_QUIZ_CONTROLLER)
public class MateriQuizController {
	
	@Autowired
	private MateriQuizService materiQuizService;
	
	@GetMapping(SystemRestConstant.MateriQuizConstant.DOWNLOAD_FILE_ADDR)
	public ResponseEntity<Resource> downloadMateriQuiz(@RequestParam(name = "name", defaultValue = "", required = true) String name) throws Exception {		
		Resource file = materiQuizService.download(name);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	
	@PostMapping(SystemRestConstant.MateriQuizConstant.SEARCH_MATERI_QUIZ_ADDR)
	public String searchHistoryLogin(@RequestBody MateriQuizSearchRequest request) throws Exception {				
		return materiQuizService.searchMateriQuiz(request);
	}
	
	@PostMapping(SystemRestConstant.MateriQuizConstant.FIND_MATERI_QUIZ_DETAIL_ADDR)
	public String findUserDatail(@RequestBody MateriQuizEntity request) throws Exception {
		return materiQuizService.findOneById(request.getId());
	}
	
	@PostMapping(SystemRestConstant.MateriQuizConstant.SAVE_MATERI_QUIZ_HEADER_ADDR)
	public String saveMateriHeader(@RequestBody MateriQuizEntity request) throws Exception {
		return materiQuizService.saveHeader(request);
	}
	
	@PostMapping(SystemRestConstant.MateriQuizConstant.SAVE_MATERI_QUIZ_ADDR)
	public String saveMateriQuiz(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) throws Exception {
		return materiQuizService.save(file, id);
	}
}
