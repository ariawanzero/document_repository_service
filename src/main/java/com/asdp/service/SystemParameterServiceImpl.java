package com.asdp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.asdp.entity.SystemParameterEntity;
import com.asdp.repository.SystemParameterRepository;
import com.asdp.util.CommonResponse;
import com.asdp.util.JsonFilter;
import com.asdp.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectWriter;

public class SystemParameterServiceImpl implements SystemParameterService{

	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	@Autowired
	private SystemParameterRepository systemParameterRepo;

	@Override
	public String findSysParamByType(String type) throws Exception{
		List<SystemParameterEntity> listSysParam = systemParameterRepo.findByTypeOrderByCodeAsc(type);
		
		CommonResponse<List<SystemParameterEntity>> response = new CommonResponse<>(listSysParam);
		ObjectWriter writter = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(SystemParameterEntity.Constant.JSON_FILTER));

		return writter.writeValueAsString(response);
	}
	
}
