package com.asdp.service;

import com.asdp.entity.UserEntity;
import com.asdp.request.ChangePasswordRequest;
import com.asdp.request.HistoryLoginRequest;
import com.asdp.request.UserSearchRequest;

public interface UserService {
	String searchUsers(UserSearchRequest request)throws Exception;
	String findOneById(String id) throws Exception;
	String saveUser(UserEntity request) throws Exception;
	
	String searchHistoryLoginUsers(HistoryLoginRequest request)throws Exception;
	String changePassword(ChangePasswordRequest request)throws Exception;
}
