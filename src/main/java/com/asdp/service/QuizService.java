package com.asdp.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.asdp.entity.QuestionEntity;
import com.asdp.entity.QuizEntity;
import com.asdp.request.QuestionRequest;
import com.asdp.request.QuizSearchRequest;

public interface QuizService {
	Resource download(String nameFile) throws Exception;
	String searchMateriQuiz(QuizSearchRequest request) throws Exception;
	String searchMateriQuestion(QuestionRequest request) throws Exception;
	String findOneById(String id) throws Exception;
	String saveHeader(QuizEntity request) throws Exception;
	String save(MultipartFile file, String id) throws Exception;
	String saveQuizWithQuestion(QuestionEntity request) throws Exception;
	String startQuiz(String id) throws Exception;
	String answerQuiz(QuestionEntity question) throws Exception;
	String publishQuiz(QuizEntity request) throws Exception;
}
