package com.asdp.service;

import org.springframework.core.io.Resource;

import com.asdp.request.MateriQuizSaveRequest;
import com.asdp.request.MateriQuizSearchRequest;

public interface MateriQuizService {
	public String save(MateriQuizSaveRequest request) throws Exception;
	public Resource download(String nameFile) throws Exception;
	public String searchMateriQuiz(MateriQuizSearchRequest request) throws Exception;
	public String findOneById(String id) throws Exception;
}
