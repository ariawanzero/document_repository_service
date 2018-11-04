package com.asdp.service;

import com.asdp.entity.QuizEntity;
import com.asdp.request.QuizSearchRequest;

public interface QuizService {
	public String findOneById(String id) throws Exception;
	String searchQuiz(QuizSearchRequest request)throws Exception;
	String saveQuiz(QuizEntity request) throws Exception;
	String saveQuizWithQuestion(QuizEntity request) throws Exception;
	String saveResultQuiz(String id) throws Exception;
}
