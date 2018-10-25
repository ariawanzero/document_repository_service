package com.asdp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.asdp.entity.UserEntity;
import com.asdp.repository.UserRepository;
import com.asdp.request.UserSearchRequest;
import com.asdp.util.CommonPageUtil;
import com.asdp.util.CommonPaging;
import com.asdp.util.CommonResponseGenerator;
import com.asdp.util.CommonResponsePaging;
import com.asdp.util.DateTimeFunction;
import com.asdp.util.JsonFilter;
import com.asdp.util.JsonUtil;
import com.asdp.util.StringFunction;
import com.asdp.util.SystemConstant;
import com.asdp.util.SystemConstant.StatusConstants;
import com.asdp.util.SystemConstant.UserRoleConstants;
import com.asdp.util.SystemConstant.ValidFlag;
import com.fasterxml.jackson.databind.ObjectWriter;

public class UserServiceImpl implements UserDetailsService, UserService {
	
	private final String SPACE = " ";
	private final String SUMIN = "superuser";
	private final String PREFIX_ROLE = "ROLE_";
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private CommonPageUtil pageUtil;
	
	@Autowired
	private CommonResponseGenerator comGen;
	
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
		return new User(user.getUsername(), user.getPassword(), getAuthority(user.getUserRole().getRoleName()));
	}
	
	private List<SimpleGrantedAuthority> getAuthority(String role) {
		return Arrays.asList(new SimpleGrantedAuthority(PREFIX_ROLE + role));
	}

	@Override
	public String searchUsers(UserSearchRequest request) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		UserEntity users = new UserEntity();
		BeanUtils.copyProperties(principal, users);
		
		Pageable pageable = pageUtil.generateDefaultPageRequest(request.getPage(),
				new Sort(Sort.Direction.ASC, UserEntity.Constant.USERNAME_FIELD));
		List<UserEntity> listExpired = new ArrayList<>();
		
		Specification<UserEntity> spec = new Specification<UserEntity>() {
			private static final long serialVersionUID = 8171063252296440986L;

			@Override
			public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				//list.add(cb.equal(root.<Integer>get(UserEntity.Constant.VALID_FIELD), SystemConstant.ValidFlag.VALID));
				list.add(cb.notEqual(cb.lower(root.<String>get(UserEntity.Constant.USERNAME_FIELD)),
						SUMIN.toLowerCase()));
				list.add(cb.notEqual(cb.lower(root.<String>get(UserEntity.Constant.USERNAME_FIELD)),
						users.getUsername().toLowerCase()));
				
				if (!StringFunction.isEmpty(request.getUsername())) {
					list.add(cb.like(cb.lower(root.<String>get(UserEntity.Constant.USERNAME_FIELD)),
							SystemConstant.WILDCARD + request.getUsername().toLowerCase() + SystemConstant.WILDCARD));
				}
				
				if (!StringFunction.isEmpty(request.getName())) {
					list.add(cb.like(cb.lower(root.<String>get(UserEntity.Constant.NAME_FIELD)),
							SystemConstant.WILDCARD + request.getName().toLowerCase() + SystemConstant.WILDCARD));
				}
				if (!StringFunction.isEmpty(request.getJabatan())) {
					list.add(cb.like(cb.lower(root.<String>get(UserEntity.Constant.JABATAN_FIELD)),
							SystemConstant.WILDCARD + request.getJabatan().toLowerCase() + SystemConstant.WILDCARD));
				}
				if (!StringFunction.isEmpty(request.getDivisi())) {
					list.add(cb.like(cb.lower(root.<String>get(UserEntity.Constant.DIVISI_FIELD)),
							SystemConstant.WILDCARD + request.getDivisi().toLowerCase() + SystemConstant.WILDCARD));
				}
				if (!StringFunction.isEmpty(request.getStatus())) {
					if(request.getStatus().equals(StatusConstants.ACTIVE)){
						list.add(cb.equal(root.<Integer>get(UserEntity.Constant.VALID_FIELD), ValidFlag.VALID));
						list.add(cb.greaterThan(root.get(UserEntity.Constant.EXPIRED_DATE_FIELD), new Date()));
					}else{
						list.add(cb.equal(root.<Integer>get(UserEntity.Constant.VALID_FIELD), ValidFlag.INVALID));
					}
				}
				return cb.and(list.toArray(new Predicate[] {}));
			}
		};
		Page<UserEntity> paging = userRepo.findAll(spec, pageable);
		
		paging.getContent().stream().map(user -> {
			user.setUserRoleId(user.getUserRole().getUserRoleCode());
			user.setUserRoleName(user.getUserRole().getRoleName());
			user.setPosition(user.getJabatan().concat(SPACE).concat(user.getDivisi()).concat(SPACE).concat(user.getUnit()));
			if(user.getValid() == 1 && !DateTimeFunction.getExpiredDate(user.getExpiredDate())){
				user.setStatus(StatusConstants.ACTIVE);
			}else{
				if(DateTimeFunction.getExpiredDate(user.getExpiredDate())){
					user.setValid(SystemConstant.ValidFlag.INVALID);
					listExpired.add(user);
				}
				user.setStatus(StatusConstants.EXPIRED);
			}
			return user;
		}).collect(Collectors.toList());
		
		if(listExpired.size() > 0) updateStatusInvalid(listExpired);
		
		if(!StringFunction.isEmpty(request.getUserRole())){
			 paging = PageableExecutionUtils.getPage(
			    		paging.getContent().stream().filter(a -> a.getUserRoleId().equals(request.getUserRole()))
						.collect(Collectors.toList()),
						pageable,
						paging::getTotalElements);
		}
	   
		
		CommonResponsePaging<UserEntity> restResponse = comGen
				.generateCommonResponsePaging(new CommonPaging<>(paging));
		
		ObjectWriter writer = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(UserEntity.Constant.JSON_FILTER),
				new JsonFilter(UserEntity.Constant.JSON_FILTER, UserEntity.Constant.USER_ROLE_FIELD, UserEntity.Constant.PASSWORD_FIELD));
		
		return writer.writeValueAsString(restResponse);
	}
	
	@Async
	private void updateStatusInvalid(List<UserEntity> listUser){
		userRepo.saveAll(listUser);
	}
}
