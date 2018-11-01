package com.asdp.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.asdp.entity.MateriQuizEntity;
import com.asdp.request.MateriQuizSearchRequest;

public interface MateriQuizService {
	public Resource download(String nameFile) throws Exception;
	String searchMateriQuiz(MateriQuizSearchRequest request) throws Exception;
	String findOneById(String id) throws Exception;
	String saveHeader(MateriQuizEntity request) throws Exception;
	String save(MultipartFile file, String id) throws Exception;
}
