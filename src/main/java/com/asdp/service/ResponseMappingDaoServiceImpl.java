package com.asdp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.asdp.entity.ResponseMappingEntity;
import com.asdp.repository.ResponseMappingRepo;

public class ResponseMappingDaoServiceImpl implements ResponseMappingDaoService {
	@Autowired
	ResponseMappingRepo appConfRepo;

	@Override
	public List<ResponseMappingEntity> getAllResponseMapping() {
		List<ResponseMappingEntity> lst = new ArrayList<ResponseMappingEntity>();
		this.appConfRepo.findAll().forEach((resp) -> {
			lst.add(resp);
		});
		return lst;
	}
}
