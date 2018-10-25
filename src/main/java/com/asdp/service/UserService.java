package com.asdp.service;

import com.asdp.request.UserSearchRequest;

public interface UserService {
	String searchUsers(UserSearchRequest request)throws Exception;
}
