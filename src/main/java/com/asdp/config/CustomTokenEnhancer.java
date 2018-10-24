package com.asdp.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.asdp.entity.MenuEntity;
import com.asdp.entity.UserEntity;
import com.asdp.repository.UserRepository;
import com.asdp.service.MenuService;
import com.asdp.util.JsonFilter;
import com.asdp.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class CustomTokenEnhancer implements TokenEnhancer {
	
	@Autowired
	public MenuService menuService;
	
	@Autowired
	public UserRepository userRepo;

	@SuppressWarnings("unchecked")
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		Map<String, Object> addInfo = new HashMap<>();
		UserEntity userRole;
		try {
			userRole = (UserEntity) userRepo.findByUsername(user.getUsername());
			List<MenuEntity> listMenu = menuService.getMenuByUser(userRole.getUserRole().getUserRoleCode());
			ObjectWriter write = JsonUtil.generateJsonWriterWithFilter(
					new JsonFilter(MenuEntity.Constant.JSON_FILTER),
					new JsonFilter(MenuEntity.Constant.JSON_FILTER, MenuEntity.Constant.MENU_ROLES_FIELD));
			
			ObjectMapper om = JsonUtil.generateDefaultJsonMapper();
			List<MenuEntity> listMenu2 = om.readValue(write.writeValueAsString(listMenu), List.class);
			
			addInfo.put("menu", listMenu2);
			addInfo.put("clientEmail", userRole.getUsername());
			addInfo.put("clientName", userRole.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(addInfo);
		return accessToken;
	}
}
