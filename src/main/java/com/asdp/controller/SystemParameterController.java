package com.asdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asdp.entity.SystemParameterEntity;
import com.asdp.request.SystemParameterRequest;
import com.asdp.service.SystemParameterService;
import com.asdp.util.SystemRestConstant;

@RestController
@RequestMapping(SystemRestConstant.SysParamConstant.SYS_PARAM_CONTROLLER)
public class SystemParameterController {
	
	@Autowired
	private SystemParameterService systemParameterService;
	
	@PostMapping(SystemRestConstant.SysParamConstant.FIND_PARAM_BY_TYPE_CONTROLLER)
	public String searchResultQuiz(@RequestBody SystemParameterEntity request) throws Exception {				
		return systemParameterService.findSysParamByType(request.getType());
	}
	
	@PostMapping(SystemRestConstant.SysParamConstant.SAVE_PARAM_ADDR)
	public String saveDocument(@RequestBody SystemParameterEntity request) throws Exception {				
		return systemParameterService.saveSysParam(request);
	}

	@PostMapping(SystemRestConstant.SysParamConstant.FIND_PARAM_DETAIL_ADDR)
	public String findDocumentDetail(@RequestBody SystemParameterEntity request) throws Exception {
		return systemParameterService.findSysParamDetail(request.getCode());
	}
	
	@PostMapping(SystemRestConstant.SysParamConstant.SEARCH_PARAM_CONTROLLER)
	public String searchDocument(@RequestBody SystemParameterRequest request) throws Exception {
		return systemParameterService.searchSysParam(request);
	}
}
