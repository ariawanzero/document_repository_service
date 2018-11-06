package com.asdp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import com.asdp.entity.MateriQuizEntity;
import com.asdp.entity.UserEntity;
import com.asdp.repository.MateriQuizRepository;
import com.asdp.request.MateriQuizSearchRequest;
import com.asdp.util.CommonPageUtil;
import com.asdp.util.CommonPaging;
import com.asdp.util.CommonResponse;
import com.asdp.util.CommonResponseGenerator;
import com.asdp.util.CommonResponsePaging;
import com.asdp.util.JsonFilter;
import com.asdp.util.JsonUtil;
import com.asdp.util.StringFunction;
import com.asdp.util.SystemConstant;
import com.asdp.util.SystemConstant.UploadConstants;
import com.asdp.util.SystemRestConstant.MateriQuizConstant;
import com.asdp.util.SystemRestConstant.OpenFileConstant;
import com.asdp.util.UserException;
import com.fasterxml.jackson.databind.ObjectWriter;

import liquibase.util.file.FilenameUtils;

public class MateriQuizServiceImpl implements MateriQuizService{

	Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private CommonResponseGenerator comGen;
	
	@Autowired
	private MateriQuizRepository materiQuizRepo;

	@Autowired
	private CommonPageUtil pageUtil;
	
	@Autowired
	StorageService storageService;
	
	@SuppressWarnings("unchecked")
	@Override
	public String save(MultipartFile file, String id) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		UserEntity users = new UserEntity();
		BeanUtils.copyProperties(principal, users);
		
		Optional<MateriQuizEntity> en = materiQuizRepo.findById(id);
		List<String> fileName = new ArrayList<>();
		
		if(en.get() != null) {
			if(en.get().getNameFileJson() != null) {
				fileName = JsonUtil.parseJson(en.get().getNameFileJson(), ArrayList.class);
			}
			
			try {
				String name = en.get().getName().concat("-")
												.concat(String.valueOf(fileName.size() + 1))
												.concat(".")
												.concat(FilenameUtils.getExtension(file.getOriginalFilename()));
				fileName.add(name);
				storageService.store(file, name);
			} catch (Exception e) {
				throw new UserException("400", "Fail Transfer File to Server !");
			}
			
			MateriQuizEntity materi = en.get();
			materi.setNameFileJson(JsonUtil.generateJson(fileName));
			materi.setModifiedBy(users.getUsername());
			materi.setModifiedDate(new Date());
			materiQuizRepo.save(materi);
		} else {
			throw new UserException("400", "Data not Exists");
		}
		
		
		CommonResponse<String> response = comGen.generateCommonResponse(SystemConstant.SUCCESS);
		return JsonUtil.generateDefaultJsonWriter().writeValueAsString(response);
	}

	@Override
	public Resource download(String nameFile) throws Exception {
		return storageService.loadFile(nameFile);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String searchMateriQuiz(MateriQuizSearchRequest request) throws Exception {
		Pageable pageable = pageUtil.generateDefaultPageRequest(request.getPage(),
				new Sort(Sort.Direction.ASC, MateriQuizEntity.Constant.NAME_FIELD));
		
		Specification<MateriQuizEntity> spec = (root, query, criteriaBuilder) -> {
			List<Predicate> list = new ArrayList<>();
			
			if (!StringFunction.isEmpty(request.getName())) {
				list.add(criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(MateriQuizEntity.Constant.NAME_FIELD)),
						SystemConstant.WILDCARD + request.getName().toLowerCase() + SystemConstant.WILDCARD));
			}
			
			return criteriaBuilder.and(list.toArray(new Predicate[] {}));
		};
		
		Page<MateriQuizEntity> paging = materiQuizRepo.findAll(spec, pageable);
		
		paging.getContent().stream().map(materi -> {
			if(materi.getNameFileJson() != null) {
				try {
					materi.setNameFile(JsonUtil.parseJson(materi.getNameFileJson(), ArrayList.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return materi;
		}).collect(Collectors.toList());
		
		CommonResponsePaging<MateriQuizEntity> restResponse = comGen
				.generateCommonResponsePaging(new CommonPaging<>(paging));
		
		ObjectWriter writer = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(MateriQuizEntity.Constant.JSON_FILTER),
				new JsonFilter(MateriQuizEntity.Constant.JSON_FILTER, MateriQuizEntity.Constant.NAME_FILE_JSON_FIELD));
		
		return writer.writeValueAsString(restResponse);
	}
	
	private boolean isExistMateriByMateriName(String name, String id) {
		Specification<MateriQuizEntity> spec = (root, query, criteriaBuilder) -> {
			List<Predicate> list = new ArrayList<>();
			list.add(criteriaBuilder.equal(root.<Integer>get(MateriQuizEntity.Constant.VALID_FIELD), SystemConstant.ValidFlag.VALID));
			list.add(criteriaBuilder.equal(criteriaBuilder.lower(root.<String>get(MateriQuizEntity.Constant.NAME_FIELD)),
					name.toLowerCase()));
			
			if(id != null) {
				list.add(criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(MateriQuizEntity.Constant.ID_FIELD)),
						SystemConstant.WILDCARD + id.toLowerCase() + SystemConstant.WILDCARD));
			}
			return criteriaBuilder.and(list.toArray(new Predicate[] {}));
		};
		
		Long rowCount = materiQuizRepo.count(spec);
		return (rowCount != null && rowCount > 0 ? true : false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String findOneById(String id) throws Exception {
		Optional<MateriQuizEntity> materi = materiQuizRepo.findById(id);
		
		if (materi.get() == null) throw new UserException("400", "User not found");
		
		if(materi.get().getNameFileJson() != null) {
			materi.get().setNameFile(JsonUtil.parseJson(materi.get().getNameFileJson(), ArrayList.class));
		}
		materi.get().setUrlPreview(UploadConstants.URL_PREVIEW.concat(OpenFileConstant.OPEN_CONTROLLER)
				.concat(MateriQuizConstant.PREVIEW_FILE_ADDR).concat("?name="));
		
		CommonResponse<MateriQuizEntity> response = new CommonResponse<>(materi.get());
		ObjectWriter writter = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(MateriQuizEntity.Constant.JSON_FILTER),
				new JsonFilter(MateriQuizEntity.Constant.JSON_FILTER, MateriQuizEntity.Constant.NAME_FILE_JSON_FIELD));

		return writter.writeValueAsString(response);
	}

	@Override
	public String saveHeader(MateriQuizEntity request) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		UserEntity users = new UserEntity();
		BeanUtils.copyProperties(principal, users);
		
		if (request == null || StringFunction.isEmpty(request.getName())) {
			throw new UserException("400", "Materi Name is mandatory !");
		}
		if (isExistMateriByMateriName(request.getName(), request.getId())) {
			throw new UserException("400", "Materi with that Name already exists !");
		}
		
		MateriQuizEntity materi = new MateriQuizEntity();
		materi.setName(request.getName());
		materi.setCreatedBy(users.getUsername());
		materi.setCreatedDate(new Date());
		materiQuizRepo.save(materi);
		
		CommonResponse<String> response = comGen.generateCommonResponse(SystemConstant.SUCCESS);
		return JsonUtil.generateDefaultJsonWriter().writeValueAsString(response);
	}

}
