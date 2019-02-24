package com.asdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asdp.entity.UserEntity;
import com.asdp.request.ChangePasswordRequest;
import com.asdp.request.HistoryLoginRequest;
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
	
	@PostMapping(SystemRestConstant.UserConstant.FIND_USER_DETAIL_ADDR)
	public String findUserDatail(@RequestBody UserEntity request) throws Exception {
		return userService.findOneById(request.getId());
	}
	
	@PostMapping(SystemRestConstant.UserConstant.SAVE_USER_ADDR)
	public String saveUser(@RequestBody UserEntity request) throws Exception {
		return userService.saveUser(request);
	}
	
	@PostMapping(SystemRestConstant.UserConstant.SEARCH_HISTORY_LOGIN_ADDR)
	public String searchHistoryLogin(@RequestBody HistoryLoginRequest request) throws Exception {				
		return userService.searchHistoryLoginUsers(request);
	}
	
	@PostMapping(SystemRestConstant.UserConstant.COUNT_HISTORY_LOGIN_ADDR)
	public String searchHistoryLogin() throws Exception {				
		return userService.countHistoryLoginUsers();
	}
	
	@PostMapping(SystemRestConstant.UserConstant.CHANGE_PASSWORD_ADDR)
	public String changePassword(@RequestBody ChangePasswordRequest request) throws Exception {				
		return userService.changePassword(request);
	}
}
