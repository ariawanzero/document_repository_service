package com.asdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asdp.entity.QuizEntity;
import com.asdp.service.QuizService;
import com.asdp.util.SystemRestConstant;

@RestController
@RequestMapping(SystemRestConstant.QuizConstant.QUIZ_CONTROLLER)
public class QuizController {

	@Autowired
	private QuizService quizService;
	
	@PostMapping(SystemRestConstant.QuizConstant.SAVE_QUIZ_ADDR)
	public String saveMateriHeader(@RequestBody QuizEntity request) throws Exception {
		return quizService.saveQuiz(request);
	}
}
