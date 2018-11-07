package com.asdp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import com.asdp.entity.QuestionEntity;
import com.asdp.entity.QuizEntity;
import com.asdp.entity.UserEntity;
import com.asdp.repository.QuestionRepository;
import com.asdp.repository.QuizRepository;
import com.asdp.request.QuizSearchRequest;
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
import com.asdp.util.SystemConstant.ValidFlag;
import com.asdp.util.SystemRestConstant.OpenFileConstant;
import com.asdp.util.SystemRestConstant.QuizConstant;
import com.asdp.util.UserException;
import com.fasterxml.jackson.databind.ObjectWriter;

import liquibase.util.file.FilenameUtils;

public class QuizServiceImpl implements QuizService{

	Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private CommonResponseGenerator comGen;
	
	@Autowired
	private QuizRepository quizRepo;
	
	@Autowired
	private QuestionRepository questionRepo;

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
		
		QuizEntity en = quizRepo.findById(id).orElseThrow(() -> new UserException("400", "Data not Exists"));
		List<String> fileName = new ArrayList<>();
		
		if(en.getNameFileJson() != null) {
			fileName = JsonUtil.parseJson(en.getNameFileJson(), ArrayList.class);
		}
		
		try {
			String name = en.getName()
							.concat("-")
							.concat(String.valueOf(fileName.size() + 1))
							.concat(".")
							.concat(FilenameUtils.getExtension(file.getOriginalFilename()));
			
			fileName.add(name);
			storageService.store(file, name);
		} catch (Exception e) {
			throw new UserException("400", "Fail Transfer File to Server !");
		}
		
		QuizEntity materi = en;
		materi.setNameFileJson(JsonUtil.generateJson(fileName));
		materi.setModifiedBy(users.getUsername());
		materi.setModifiedDate(new Date());
		quizRepo.save(materi);
		
		CommonResponse<String> response = comGen.generateCommonResponse(SystemConstant.SUCCESS);
		return JsonUtil.generateDefaultJsonWriter().writeValueAsString(response);
	}

	@Override
	public Resource download(String nameFile) throws Exception {
		return storageService.loadFile(nameFile);
	}

	@Override
	public String searchMateriQuiz(QuizSearchRequest request) throws Exception {
		//List<QuizEntity> listExpired = new ArrayList<>();
		
		Pageable pageable = pageUtil.generateDefaultPageRequest(request.getPage(),
				new Sort(Sort.Direction.ASC, QuizEntity.Constant.NAME_FIELD));
		
		Specification<QuizEntity> spec = (root, query, criteriaBuilder) -> {
			List<Predicate> list = new ArrayList<>();
			list.add(criteriaBuilder.equal(root.<String>get(QuizEntity.Constant.VALID_FIELD),
						ValidFlag.VALID));
			
			if (!StringFunction.isEmpty(request.getName())) {
				list.add(criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(QuizEntity.Constant.NAME_FIELD)),
						SystemConstant.WILDCARD + request.getName().toLowerCase() + SystemConstant.WILDCARD));
			}
			
			if (!StringFunction.isEmpty(request.getDivisi())) {
				list.add(criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(QuizEntity.Constant.DIVISI_FIELD)),
						SystemConstant.WILDCARD + request.getDivisi().toLowerCase() + SystemConstant.WILDCARD));
			}
			
			return criteriaBuilder.and(list.toArray(new Predicate[] {}));
		};
		
		Page<QuizEntity> paging = quizRepo.findAll(spec, pageable);
		/*paging.getContent().stream().map(quiz -> {
			if(DateTimeFunction.getExpiredDate(quiz.getStartDate())){
				quiz.setPassQuiz(ValidFlag.INVALID);
				listExpired.add(quiz);
			}
			return quiz;
		}).collect(Collectors.toList());
		
		if(listExpired.size() > 0) updateStatusInvalid(listExpired);*/
		
		CommonResponsePaging<QuizEntity> restResponse = comGen
				.generateCommonResponsePaging(new CommonPaging<>(paging));
		
		ObjectWriter writer = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(QuizEntity.Constant.JSON_FILTER),
				new JsonFilter(QuizEntity.Constant.JSON_FILTER, QuizEntity.Constant.QUESTION_FIELD));
		
		return writer.writeValueAsString(restResponse);
	}
	
	@Async
	private void updateStatusInvalid(List<QuizEntity> listQuiz){
		quizRepo.saveAll(listQuiz);
	}
	
	private boolean isExistMateriByMateriName(String name, String id) {
		Specification<QuizEntity> spec = (root, query, criteriaBuilder) -> {
			List<Predicate> list = new ArrayList<>();
			list.add(criteriaBuilder.equal(root.<Integer>get(QuizEntity.Constant.VALID_FIELD), SystemConstant.ValidFlag.VALID));
			list.add(criteriaBuilder.equal(criteriaBuilder.lower(root.<String>get(QuizEntity.Constant.NAME_FIELD)),
					name.toLowerCase()));
			
			if(id != null) {
				list.add(criteriaBuilder.notEqual(root.<String>get(QuizEntity.Constant.ID_FIELD),id.toLowerCase()));
			}
			return criteriaBuilder.and(list.toArray(new Predicate[] {}));
		};
		
		Long rowCount = quizRepo.count(spec);
		return (rowCount != null && rowCount > 0 ? true : false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String findOneById(String id) throws Exception {
		QuizEntity materi = quizRepo.findById(id).orElseThrow(() -> new UserException("400", "User not found"));
		
		if(materi.getNameFileJson() != null) {
			materi.setNameFile(JsonUtil.parseJson(materi.getNameFileJson(), ArrayList.class));
		}
		materi.setUrlPreview(UploadConstants.URL_PREVIEW.concat(OpenFileConstant.OPEN_CONTROLLER)
				.concat(QuizConstant.PREVIEW_FILE_ADDR).concat("?name="));
		
		CommonResponse<QuizEntity> response = new CommonResponse<>(materi);
		ObjectWriter writter = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(QuizEntity.Constant.JSON_FILTER),
				new JsonFilter(QuizEntity.Constant.JSON_FILTER, QuizEntity.Constant.NAME_FILE_JSON_FIELD, QuizEntity.Constant.QUESTION_FIELD));

		return writter.writeValueAsString(response);
	}

	@Override
	public String saveHeader(QuizEntity request) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		UserEntity users = new UserEntity();
		BeanUtils.copyProperties(principal, users);
		
		QuizEntity toUpdate = request;
		if (request == null || StringFunction.isEmpty(request.getName())) {
			throw new UserException("400", "Quiz Name is mandatory !");
		}
		if (isExistMateriByMateriName(request.getName(), request.getId())) {
			throw new UserException("400", "Quiz with that Name already exists !");
		}
		
		if (StringFunction.isNotEmpty(request.getId())) {
			Optional<QuizEntity> existUser = quizRepo.findById(request.getId());
			if (existUser == null) {
				throw new UserException("400", "Quiz not found !");
			} else {
				toUpdate = existUser.get();
			}
			
			BeanUtils.copyProperties(request, toUpdate);

			toUpdate.setModifiedBy(users.getUsername());
			toUpdate.setModifiedDate(new Date());
		}else {
			toUpdate.setCreatedBy(users.getUsername());
			toUpdate.setCreatedDate(new Date());
		}
		
		quizRepo.save(toUpdate);
		
		CommonResponse<String> response = comGen.generateCommonResponse(SystemConstant.SUCCESS);
		return JsonUtil.generateDefaultJsonWriter().writeValueAsString(response);
	}
	
	@Override
	public String saveQuizWithQuestion(QuizEntity request) throws Exception {
		Optional<QuizEntity> quiz = null;
		
		if (StringFunction.isEmpty(request.getId())) {
			throw new UserException("400", "Quiz is Mandatory!");
		}
		
		quiz = quizRepo.findById(request.getId());
		if (quiz == null) {
			throw new UserException("400", "Quiz not Found!");
		}
		List<QuestionEntity> questions = new ArrayList<>();
		if(request.getQuestion() != null && !request.getQuestion().isEmpty()) {
			List<QuestionEntity> temp = new ArrayList<>(request.getQuestion());

			for (QuestionEntity e : temp) {
				e.setQuiz(quiz.get());
				questions.add(e);
			}
		}
		
		if (!questions.isEmpty()) {
			questionRepo.saveAll(questions);
		}
		
		CommonResponse<String> response = comGen.generateCommonResponse(SystemConstant.SUCCESS);
		return JsonUtil.generateDefaultJsonWriter().writeValueAsString(response);
	}

	@Override
	public String saveResultQuiz(String id) throws Exception {
		/*Optional<QuizEntity> quiz = quizRepo.findById(id);
		quiz.get().getQuestionList().stream().map(question -> 
		).;
		return null;*/
		return null;
	}
}
