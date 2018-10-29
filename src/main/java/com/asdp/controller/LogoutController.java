package com.asdp.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asdp.util.CommonResponse;
import com.asdp.util.CommonResponseGenerator;
import com.asdp.util.JsonUtil;
import com.asdp.util.SystemConstant;

@RestController
@RequestMapping("/oauth")
public class LogoutController {
	
	public static final String LOGOUT_ADDR = "/logout";
	
	@Autowired
	CommonResponseGenerator comGen;
	
	@Resource(name = "tokenServices")
	ConsumerTokenServices tokenServices;
	
	@PostMapping(LOGOUT_ADDR)
	public String revokeToken() throws Exception{
		Authentication authentication = SecurityContextHolder.getContext()
			    .getAuthentication();
		CommonResponse<String> response;
		OAuth2AuthenticationDetails auth = (OAuth2AuthenticationDetails) authentication.getDetails();
		
		if (auth != null){
			tokenServices.revokeToken(auth.getTokenValue());
			response = comGen.generateCommonResponse(SystemConstant.SUCCESS);

		}else{
			response = comGen.generateCommonResponse(SystemConstant.FAILED);
		}
		return JsonUtil.generateDefaultJsonWriter().writeValueAsString(response);
	}
}
