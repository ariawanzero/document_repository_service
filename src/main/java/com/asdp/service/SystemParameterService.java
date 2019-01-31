package com.asdp.service;

import com.asdp.entity.SystemParameterEntity;
import com.asdp.request.SystemParameterRequest;

public interface SystemParameterService {
	String findSysParamByType(String type) throws Exception;
	String saveSysParam(SystemParameterEntity request) throws Exception;
	String findSysParamDetail(String id) throws Exception;
	String searchSysParam(SystemParameterRequest request) throws Exception;
}
