package com.asdp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.asdp.entity.QuizEntity;
import com.asdp.repository.QuizRepository;
import com.asdp.util.CommonResponse;
import com.asdp.util.JsonFilter;
import com.asdp.util.JsonUtil;
import com.asdp.util.UserException;
import com.fasterxml.jackson.databind.ObjectWriter;

public class QuizServiceImpl implements QuizService{

	@Autowired
	private QuizRepository quizRepo;
	
	@Override
	public String findOneById(String id) throws Exception {
		Optional<QuizEntity> quiz = quizRepo.findById(id);
		
		if (quiz.get() == null) throw new UserException("400", "Quiz not found");
		
		CommonResponse<QuizEntity> response = new CommonResponse<>(quiz.get());
		ObjectWriter writter = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(QuizEntity.Constant.JSON_FILTER),
				new JsonFilter(QuizEntity.Constant.JSON_FILTER, QuizEntity.Constant.MATERI_QUIZ));

		return writter.writeValueAsString(response);
	}

}
