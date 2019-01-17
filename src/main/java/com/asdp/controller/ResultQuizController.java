package com.asdp.controller;

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public String searchResultQuiz(@RequestBody ResultQuizSearchRequest request) throws Exception {				
		return quizService.searchResultQuiz(request);
	}
	
	@PostMapping(SystemRestConstant.QuizConstant.DETAIL_RESULT_QUIZ_CONTROLLER)
	public String findUserDetail(@RequestBody ResultQuizSearchRequest request) throws Exception {
		return quizService.detailResultQuiz(request);
	}
	
	@GetMapping(SystemRestConstant.QuizConstant.DOWNLOAD_RESULT_QUIZ_CONTROLLER)
	public void downloadResultQuiz(@RequestParam("id") String id, HttpServletResponse response) throws Exception {
		response.setContentType("application/csv");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + id.concat(".csv") + "\"");
		OutputStream outputStream = response.getOutputStream();
		outputStream.write(quizService.downloadResulQuiz(id).toByteArray());
		outputStream.flush();
		outputStream.close();
	}
}
