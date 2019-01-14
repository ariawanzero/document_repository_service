package com.asdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asdp.request.ResultQuizSearchRequest;
import com.asdp.service.QuizService;
import com.asdp.util.SystemRestConstant;

@RestController
@RequestMapping(SystemRestConstant.QuizConstant.RESULT_QUIZ_CONTROLLER)
public class ResultQuizController {
	
	@Autowired
	private QuizService quizService;
	
	@PostMapping(SystemRestConstant.QuizConstant.SEARCH_RESULT_QUIZ_CONTROLLER)
	public String searchHistoryLogin(@RequestBody ResultQuizSearchRequest request) throws Exception {				
		return quizService.searchResultQuiz(request);
	}
	
	@PostMapping(SystemRestConstant.QuizConstant.DETAIL_RESULT_QUIZ_CONTROLLER)
	public String findUserDatail(@RequestBody ResultQuizSearchRequest request) throws Exception {
		return quizService.detailResultQuiz(request);
	}
}
