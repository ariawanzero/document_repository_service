package com.asdp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.asdp.entity.QuestionEntity;
import com.asdp.repository.QuestionRepository;
import com.asdp.util.CommonResponse;
import com.asdp.util.JsonFilter;
import com.asdp.util.JsonUtil;
import com.asdp.util.UserException;
import com.fasterxml.jackson.databind.ObjectWriter;

public class QuestionServiceImpl implements QuestionService{
	
	@Autowired
	private QuestionRepository questionRepo;
	
	@Override
	public String findOneById(String id) throws Exception {
		Optional<QuestionEntity> question = questionRepo.findById(id);
		
		if (question.get() == null) throw new UserException("400", "Question not found");
		
		CommonResponse<QuestionEntity> response = new CommonResponse<>(question.get());
		ObjectWriter writter = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(QuestionEntity.Constant.JSON_FILTER),
				new JsonFilter(QuestionEntity.Constant.JSON_FILTER, QuestionEntity.Constant.QUIZ_FIELD));

		return writter.writeValueAsString(response);
	}
}
