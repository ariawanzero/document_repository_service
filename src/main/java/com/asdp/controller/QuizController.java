package com.asdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asdp.request.QuizSearchRequest;
import com.asdp.service.QuizService;
import com.asdp.util.SystemRestConstant;

@RestController
@RequestMapping(SystemRestConstant.QuizConstant.QUIZ_CONTROLLER)
public class QuizController {

	@Autowired
	private QuizService quizService;
	
	@PostMapping(SystemRestConstant.QuizConstant.SEARCH_QUIZ_ADDR)
	public String searchHistoryLogin() throws Exception {				
		return quizService.searchMateriQuiz(new QuizSearchRequest());
	}
}
