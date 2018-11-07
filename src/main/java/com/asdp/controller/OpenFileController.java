package com.asdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asdp.service.MateriQuizService;
import com.asdp.util.SystemRestConstant;
import com.google.common.io.ByteStreams;

@RestController
@RequestMapping(SystemRestConstant.OpenFileConstant.OPEN_CONTROLLER)
public class OpenFileController {
	
	@Autowired
	private MateriQuizService materiQuizService;
	
	@GetMapping(SystemRestConstant.MateriQuizConstant.PREVIEW_FILE_ADDR)
	public ResponseEntity<byte[]> downloadMateriQuiz(@RequestParam(name = "name", defaultValue = "", required = true) String name) throws Exception {		
		Resource file = materiQuizService.download(name);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		headers.add("content-disposition", "inline;filename=" + file.getFilename());
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(ByteStreams.toByteArray(file.getInputStream()), headers, HttpStatus.OK);
		return response;
	}
}
