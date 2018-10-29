package com.asdp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.Predicate;
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

import com.asdp.entity.EmailEntity;
import com.asdp.entity.HistoryLoginEntity;
import com.asdp.entity.UserEntity;
import com.asdp.entity.UserRoleEntity;
import com.asdp.repository.EmailRepository;
import com.asdp.repository.HistoryLoginRepository;
import com.asdp.repository.UserRepository;
import com.asdp.request.ChangePasswordRequest;
import com.asdp.request.HistoryLoginRequest;
import com.asdp.request.UserSearchRequest;
import com.asdp.util.CommonPageUtil;
import com.asdp.util.CommonPaging;
import com.asdp.util.CommonResponse;
import com.asdp.util.CommonResponseGenerator;
import com.asdp.util.CommonResponsePaging;
import com.asdp.util.DateTimeFunction;
import com.asdp.util.EmailUtils;
import com.asdp.util.JsonFilter;
import com.asdp.util.JsonUtil;
import com.asdp.util.PasswordUtils;
import com.asdp.util.StringFunction;
import com.asdp.util.SystemConstant;
import com.asdp.util.UserException;
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
	private HistoryLoginRepository hisRepo;
	
	@Autowired
	private EmailRepository emailRepo;
	
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
		
		Specification<UserEntity> spec = (root, query, criteriaBuilder) -> {
			List<Predicate> list = new ArrayList<>();
			list.add(criteriaBuilder.notEqual(criteriaBuilder.lower(root.<String>get(UserEntity.Constant.USERNAME_FIELD)),
					SUMIN.toLowerCase()));
			list.add(criteriaBuilder.notEqual(criteriaBuilder.lower(root.<String>get(UserEntity.Constant.USERNAME_FIELD)),
					users.getUsername().toLowerCase()));
			
			if (!StringFunction.isEmpty(request.getUsername())) {
				list.add(criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(UserEntity.Constant.USERNAME_FIELD)),
						SystemConstant.WILDCARD + request.getUsername().toLowerCase() + SystemConstant.WILDCARD));
			}
			
			if (!StringFunction.isEmpty(request.getName())) {
				list.add(criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(UserEntity.Constant.NAME_FIELD)),
						SystemConstant.WILDCARD + request.getName().toLowerCase() + SystemConstant.WILDCARD));
			}
			if (!StringFunction.isEmpty(request.getJabatan())) {
				list.add(criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(UserEntity.Constant.JABATAN_FIELD)),
						SystemConstant.WILDCARD + request.getJabatan().toLowerCase() + SystemConstant.WILDCARD));
			}
			if (!StringFunction.isEmpty(request.getDivisi())) {
				list.add(criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(UserEntity.Constant.DIVISI_FIELD)),
						SystemConstant.WILDCARD + request.getDivisi().toLowerCase() + SystemConstant.WILDCARD));
			}
			if (!StringFunction.isEmpty(request.getStatus())) {
				if(request.getStatus().equals(StatusConstants.ACTIVE)){
					list.add(criteriaBuilder.equal(root.<Integer>get(UserEntity.Constant.VALID_FIELD), ValidFlag.VALID));
					list.add(criteriaBuilder.greaterThan(root.get(UserEntity.Constant.EXPIRED_DATE_FIELD), new Date()));
				}else{
					list.add(criteriaBuilder.equal(root.<Integer>get(UserEntity.Constant.VALID_FIELD), ValidFlag.INVALID));
				}
			}
			return criteriaBuilder.and(list.toArray(new Predicate[] {}));
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

	@Override
	public String findOneById(String id) throws Exception {
		Optional<UserEntity> user = userRepo.findById(id);
		
		if (user.get() == null) throw new UserException("400", "User not found");
		
		user.get().setUserRoleName(user.get().getUserRole().getRoleName());
		user.get().setUserRoleId(user.get().getUserRole().getUserRoleCode());

		CommonResponse<UserEntity> response = new CommonResponse<>(user.get());
		ObjectWriter writter = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(UserEntity.Constant.JSON_FILTER),
				new JsonFilter(UserEntity.Constant.JSON_FILTER, UserEntity.Constant.USER_ROLE_FIELD, UserEntity.Constant.PASSWORD_FIELD),
				new JsonFilter(UserEntity.Constant.EXPIRED_DATE_FIELD));

		return writter.writeValueAsString(response);
	}

	@Override
	public String saveUser(UserEntity request) throws Exception {
		UserEntity toUpdate = request;
		if (request == null || StringFunction.isEmpty(request.getUsername())) {
			throw new UserException("400", "User ID is mandatory !");
		}
		if (isExistUserByUserId(request.getUsername(), request.getId())) {
			throw new UserException("400", "User with that User ID already exists !");
		}
		
		UserRoleEntity userRole = new UserRoleEntity();
		userRole.setUserRoleCode(request.getUserRoleId());
		toUpdate.setUserRole(userRole);
		
		if (StringFunction.isNotEmpty(request.getId())) {
			Optional<UserEntity> existUser = userRepo.findById(request.getId());
			if (existUser == null) {
				throw new UserException("400", "User not found !");
			} else {
				toUpdate = existUser.get();
			}
			
			BeanUtils.copyProperties(request, toUpdate);
		}else{
			String password = StringFunction.randomAlphaNumeric(7);
			toUpdate.setPassword(PasswordUtils.encryptPassword(password));
			Optional<EmailEntity> email = emailRepo.findById("NEWMEMBER");
			EmailUtils.sendEmail(toUpdate.getUsername(), String.format(email.get().getBodyMessage(), toUpdate.getName(), toUpdate.getUsername(), password), email.get().getSubject());
		}
		userRepo.save(toUpdate);
		
		CommonResponse<String> response = comGen.generateCommonResponse(SystemConstant.SUCCESS);
		return JsonUtil.generateDefaultJsonWriter().writeValueAsString(response);
	}
	
	private boolean isExistUserByUserId(String userName, String id) {
		Specification<UserEntity> spec = (root, query, criteriaBuilder) -> {
			List<Predicate> list = new ArrayList<>();
			list.add(criteriaBuilder.equal(root.<Integer>get(UserEntity.Constant.VALID_FIELD), SystemConstant.ValidFlag.VALID));
			list.add(criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(UserEntity.Constant.USERNAME_FIELD)),
					SystemConstant.WILDCARD + userName.toLowerCase() + SystemConstant.WILDCARD));
			
			if(id != null) {
				list.add(criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(UserEntity.Constant.ID_FIELD)),
						SystemConstant.WILDCARD + id.toLowerCase() + SystemConstant.WILDCARD));
			}
			return criteriaBuilder.and(list.toArray(new Predicate[] {}));
		};
		
		Long rowCount = userRepo.count(spec);
		return (rowCount != null && rowCount > 0 ? true : false);
	}

	@Override
	public String searchHistoryLoginUsers(HistoryLoginRequest request) throws Exception {
		Pageable pageable = pageUtil.generateDefaultPageRequest(request.getPage(),
				new Sort(Sort.Direction.ASC, HistoryLoginEntity.Constant.DATE_LOGIN_FIELD));
		
		Specification<HistoryLoginEntity> spec = (root, query, criteriaBuilder) -> {
			List<Predicate> list = new ArrayList<>();
			if (request.getStartDate() != null && request.getEndDate() != null) {
				list.add(criteriaBuilder.between(root.get(HistoryLoginEntity.Constant.DATE_LOGIN_FIELD), request.getStartDate(), request.getEndDate()));
			}
			return criteriaBuilder.and(list.toArray(new Predicate[] {}));
		};
		Page<HistoryLoginEntity> paging = hisRepo.findAll(spec, pageable);
		   
		paging.getContent().stream().map(history -> {
			history.setUsername(history.getUser().getUsername());
			return history;
		}).collect(Collectors.toList());
		
		CommonResponsePaging<HistoryLoginEntity> restResponse = comGen
				.generateCommonResponsePaging(new CommonPaging<>(paging));
		
		ObjectWriter writer = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(HistoryLoginEntity.Constant.JSON_FILTER),
				new JsonFilter(HistoryLoginEntity.Constant.JSON_FILTER, HistoryLoginEntity.Constant.USER_FIELD));
		
		return writer.writeValueAsString(restResponse);
	}

	@Override
	public String changePassword(ChangePasswordRequest request) throws Exception {
		UserEntity user = userRepo.findByUsername(request.getUsername());
		
		if (!PasswordUtils.matchPassword(user.getPassword(), request.getOldPassword())) {
			throw new UserException("400", "Invalid Old Password !");
		}
		
		if (request.getNewPassword().equals(request.getOldPassword())) {
			throw new UserException("400", "New Password cannot be same with Old Password !");
		}
		
		user.setPassword(PasswordUtils.encryptPassword(request.getNewPassword()));
		userRepo.save(user);
		
		Optional<EmailEntity> email = emailRepo.findById("CHANGEPASSWORD");
		EmailUtils.sendEmail(user.getUsername(), String.format(email.get().getBodyMessage(), user.getName()), email.get().getSubject());
		
		CommonResponse<String> response = comGen.generateCommonResponse(SystemConstant.SUCCESS);
		return JsonUtil.generateDefaultJsonWriter().writeValueAsString(response);
	}
}
