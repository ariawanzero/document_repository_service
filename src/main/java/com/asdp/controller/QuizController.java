package com.asdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.asdp.entity.QuestionEntity;
import com.asdp.entity.QuizEntity;
import com.asdp.request.QuestionRequest;
import com.asdp.request.QuizSearchRequest;
import com.asdp.service.QuizService;
import com.asdp.util.SystemRestConstant;


@RestController
@RequestMapping(SystemRestConstant.QuizConstant.MATERI_QUIZ_CONTROLLER)
public class QuizController {
	
	@Autowired
	private QuizService quizService;
	
	@PostMapping(SystemRestConstant.QuizConstant.SEARCH_MATERI_QUIZ_ADDR)
	public String searchHistoryLogin(@RequestBody QuizSearchRequest request) throws Exception {				
		return quizService.searchMateriQuiz(request);
	}
	
	@PostMapping(SystemRestConstant.QuizConstant.FIND_MATERI_QUIZ_DETAIL_ADDR)
	public String findUserDatail(@RequestBody QuizEntity request) throws Exception {
		return quizService.findOneById(request.getId());
	}
	
	@PostMapping(SystemRestConstant.QuizConstant.SAVE_MATERI_QUIZ_HEADER_ADDR)
	public String saveMateriHeader(@RequestBody QuizEntity request) throws Exception {
		return quizService.saveHeader(request);
	}
	
	@PostMapping(SystemRestConstant.QuizConstant.PUBLISH_QUIZ_ADDR)
	public String publishQuiz(@RequestBody QuizEntity request) throws Exception {
		return quizService.publishQuiz(request);
	}
	
	@PostMapping(SystemRestConstant.QuizConstant.UPLOAD_MATERI_QUIZ_ADDR)
	public String saveMateriQuiz(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) throws Exception {
		return quizService.save(file, id);
	}
	
	@PostMapping(SystemRestConstant.QuizConstant.SEARCH_QUESTION_ADDR)
	public String searchHistoryLogin(@RequestBody QuestionRequest request) throws Exception {				
		return quizService.searchMateriQuestion(request);
	}
	
	@PostMapping(SystemRestConstant.QuizConstant.SAVE_QUESTION_ADDR)
	public String saveQuizWithQuestions(@RequestBody QuestionEntity request) throws Exception {
		return quizService.saveQuizWithQuestion(request);
	}
	
	@PostMapping(SystemRestConstant.QuizConstant.START_QUIZ_ADDR)
	public String startQuizWithQuestions(@RequestBody QuizEntity request) throws Exception {
		return quizService.startQuiz(request.getId());
	}
}
