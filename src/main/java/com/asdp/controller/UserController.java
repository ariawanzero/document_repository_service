package com.asdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asdp.request.UserSearchRequest;
import com.asdp.service.UserService;
import com.asdp.util.SystemRestConstant;

@RestController
@RequestMapping(SystemRestConstant.UserConstant.USER_CONTROLLER)
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping(SystemRestConstant.UserConstant.SEARCH_USER_DETAIL_ADDR)
	public String searchUser(@RequestBody UserSearchRequest request) throws Exception {		
		return userService.searchUsers(request);
	}
}
