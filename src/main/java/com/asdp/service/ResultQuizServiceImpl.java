package com.asdp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.asdp.entity.ResultQuizEntity;
import com.asdp.repository.ResultQuizRepository;
import com.asdp.util.CommonResponse;
import com.asdp.util.JsonFilter;
import com.asdp.util.JsonUtil;
import com.asdp.util.UserException;
import com.fasterxml.jackson.databind.ObjectWriter;

public class ResultQuizServiceImpl implements ResultQuizService{
	@Autowired
	private ResultQuizRepository resultQuizRepo;
	
	@Override
	public String findOneById(String id) throws Exception {
		Optional<ResultQuizEntity> question = resultQuizRepo.findById(id);
		
		if (question.get() == null) throw new UserException("400", "Question not found");
		
		CommonResponse<ResultQuizEntity> response = new CommonResponse<>(question.get());
		ObjectWriter writter = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(ResultQuizEntity.Constant.JSON_FILTER));

		return writter.writeValueAsString(response);
	}
}
