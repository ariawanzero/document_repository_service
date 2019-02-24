package com.asdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asdp.request.ChangePasswordRequest;
import com.asdp.service.UserService;

@RestController
@RequestMapping("/forgot")
public class ForgotController {
	public static final String FORGOT_PASSWORD_ADDR = "/forgotPassword";
	
	@Autowired
	UserService userService;

	@PostMapping(FORGOT_PASSWORD_ADDR)
	public String forgotPassword(@RequestBody ChangePasswordRequest request) throws Exception {				
		return userService.forgotPassword(request);
	}
}
