package com.asdp.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.asdp.entity.SystemParameterEntity;
import com.asdp.repository.SystemParameterRepository;
import com.asdp.request.SystemParameterRequest;
import com.asdp.util.CommonPageUtil;
import com.asdp.util.CommonPaging;
import com.asdp.util.CommonResponse;
import com.asdp.util.CommonResponseGenerator;
import com.asdp.util.CommonResponsePaging;
import com.asdp.util.JsonFilter;
import com.asdp.util.JsonUtil;
import com.asdp.util.StringFunction;
import com.asdp.util.SystemConstant;
import com.asdp.util.UserException;
import com.fasterxml.jackson.databind.ObjectWriter;

public class SystemParameterServiceImpl implements SystemParameterService{

	Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private SystemParameterRepository systemParameterRepo;

	@Autowired
	private CommonResponseGenerator comGen;

	@Autowired
	private CommonPageUtil pageUtil;

	@Override
	public String findSysParamByType(String type) throws Exception{
		List<SystemParameterEntity> listSysParam = systemParameterRepo.findByTypeOrderByCodeAsc(type);

		CommonResponse<List<SystemParameterEntity>> response = new CommonResponse<>(listSysParam);
		ObjectWriter writter = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(SystemParameterEntity.Constant.JSON_FILTER));

		return writter.writeValueAsString(response);
	}

	@Override
	public String saveSysParam(SystemParameterEntity request) throws Exception {
		SystemParameterEntity toUpdate = request;

		if (request == null || StringFunction.isEmpty(request.getCode())) {
			throw new UserException("400", "Code is mandatory !");
		}
		if (isExistSysParamBySysParamCode(request.getCode(), request.getType())) {
			throw new UserException("400", "System Parameter with that Code already exists !");
		}

		if (StringFunction.isNotEmpty(request.getCode())) {
			SystemParameterEntity existUser = systemParameterRepo.findByCode(request.getCode());
			if (existUser != null) {
				toUpdate = existUser;
			}

			BeanUtils.copyProperties(request, toUpdate);
		}


		systemParameterRepo.save(toUpdate);

		CommonResponse<String> response = comGen.generateCommonResponse(SystemConstant.SUCCESS);
		return JsonUtil.generateDefaultJsonWriter().writeValueAsString(response);}

	@Override
	public String findSysParamDetail(String code) throws Exception {
		SystemParameterEntity sysParam = systemParameterRepo.findByCode(code);
		if(sysParam == null) {
			new UserException("400", "System Parameter not found");
		}

		CommonResponse<SystemParameterEntity> response = new CommonResponse<>(sysParam);
		ObjectWriter writter = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(SystemParameterEntity.Constant.JSON_FILTER));

		return writter.writeValueAsString(response);
	}

	@Override
	public String searchSysParam(SystemParameterRequest request) throws Exception {
		Pageable pageable = pageUtil.generateDefaultPageRequest(request.getPage(),
				new Sort(Sort.Direction.ASC, SystemParameterEntity.Constant.TYPE_FIELD));

		Specification<SystemParameterEntity> spec = (root, query, criteriaBuilder) -> {
			List<Predicate> list = new ArrayList<>();

			if (!StringFunction.isEmpty(request.getCode())) {
				list.add(criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(SystemParameterEntity.Constant.CODE_FIELD)),
						SystemConstant.WILDCARD + request.getCode().toLowerCase() + SystemConstant.WILDCARD));
			}

			if (!StringFunction.isEmpty(request.getType())) {
				list.add(criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(SystemParameterEntity.Constant.TYPE_FIELD)),
						SystemConstant.WILDCARD + request.getType().toLowerCase() + SystemConstant.WILDCARD));
			}

			return criteriaBuilder.and(list.toArray(new Predicate[] {}));
		};

		Page<SystemParameterEntity> paging = systemParameterRepo.findAll(spec, pageable);

		CommonResponsePaging<SystemParameterEntity> restResponse = comGen
				.generateCommonResponsePaging(new CommonPaging<>(paging));

		ObjectWriter writer = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(SystemParameterEntity.Constant.JSON_FILTER));

		return writer.writeValueAsString(restResponse);
	}

	private boolean isExistSysParamBySysParamCode(String code, String type) {
		Specification<SystemParameterEntity> spec = (root, query, criteriaBuilder) -> {
			List<Predicate> list = new ArrayList<>();
			list.add(criteriaBuilder.equal(criteriaBuilder.lower(root.<String>get(SystemParameterEntity.Constant.CODE_FIELD)),
					code.toLowerCase()));
			list.add(criteriaBuilder.equal(root.<String>get(SystemParameterEntity.Constant.TYPE_FIELD),type.toLowerCase()));

			return criteriaBuilder.and(list.toArray(new Predicate[] {}));
		};

		Long rowCount = systemParameterRepo.count(spec);
		return (rowCount != null && rowCount > 0 ? true : false);
	}

}
