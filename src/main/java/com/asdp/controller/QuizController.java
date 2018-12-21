package com.asdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asdp.entity.QuestionEntity;
import com.asdp.entity.QuizEntity;
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

	@PostMapping(SystemRestConstant.QuizConstant.START_QUIZ_ADDR)
	public String startQuizWithQuestions(@RequestBody QuizEntity request) throws Exception {
		return quizService.startQuiz(request.getId());
	}
	
	@PostMapping(SystemRestConstant.QuizConstant.ANSWER_QUIZ_ADDR)
	public String answerQuizWithQuestions(@RequestBody QuestionEntity request) throws Exception {
		return quizService.answerQuiz(request);
	}
	
	@PostMapping(SystemRestConstant.QuizConstant.FINISH_QUIZ_ADDR)
	public String finishQuizWithQuestions(@RequestBody QuestionEntity request) throws Exception {
		return quizService.answerQuiz(request);
	}
}
