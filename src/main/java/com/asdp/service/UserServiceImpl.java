package com.asdp.service;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.asdp.entity.UserEntity;
import com.asdp.repository.UserRepository;
import com.asdp.util.DateTimeFunction;
import com.asdp.util.SystemConstant.UserRoleConstants;
import com.asdp.util.SystemConstant.ValidFlag;

public class UserServiceImpl implements UserDetailsService, UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userRepo.findByUsername(username);
		if(user == null){
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		
		if(!user.getUserRole().getRoleName().equals(UserRoleConstants.SUPERADMIN)){
			if(DateTimeFunction.getExpiredDate(user.getExpiredDate())){
				if(user.getValid() == ValidFlag.VALID){
					user.setValid(ValidFlag.INVALID);
					userRepo.save(user);
				}
				throw new UsernameNotFoundException("Username has been Expired.");
			}
		}
		return new User(user.getUsername(), user.getPassword(), getAuthority(user));
	}
	
	private List<SimpleGrantedAuthority> getAuthority(UserEntity user) {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}
}
