package com.asdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asdp.entity.SystemParameterEntity;
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
}
