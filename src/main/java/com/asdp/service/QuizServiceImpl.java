package com.asdp.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import com.asdp.entity.EmailEntity;
import com.asdp.entity.QuestionEntity;
import com.asdp.entity.QuizEntity;
import com.asdp.entity.ResultQuizEntity;
import com.asdp.entity.UserEntity;
import com.asdp.repository.EmailRepository;
import com.asdp.repository.QuestionRepository;
import com.asdp.repository.QuizRepository;
import com.asdp.repository.ResultQuizRepository;
import com.asdp.repository.UserRepository;
import com.asdp.request.QuestionRequest;
import com.asdp.request.QuizSearchRequest;
import com.asdp.request.ResultQuizSearchRequest;
import com.asdp.util.CommonPageUtil;
import com.asdp.util.CommonPaging;
import com.asdp.util.CommonResponse;
import com.asdp.util.CommonResponseGenerator;
import com.asdp.util.CommonResponsePaging;
import com.asdp.util.DateTimeFunction;
import com.asdp.util.EmailUtils;
import com.asdp.util.JsonFilter;
import com.asdp.util.JsonUtil;
import com.asdp.util.StringFunction;
import com.asdp.util.SystemConstant;
import com.asdp.util.SystemConstant.UploadConstants;
import com.asdp.util.SystemConstant.UserRoleConstants;
import com.asdp.util.SystemConstant.ValidFlag;
import com.asdp.util.SystemRestConstant.OpenFileConstant;
import com.asdp.util.SystemRestConstant.QuizConstant;
import com.asdp.util.UserException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
	private ResultQuizRepository resultQuizRepo;

	@Autowired
	private CommonPageUtil pageUtil;

	@Autowired
	StorageService storageService;

	@Autowired
	private EmailRepository emailRepo;

	@Autowired
	private UserRepository userRepo;

	@PersistenceContext
	EntityManager em;

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
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		UserEntity users = new UserEntity();
		BeanUtils.copyProperties(principal, users);
		UserEntity user = userRepo.findByUsername(users.getUsername());

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

			if (!user.getUserRole().getRoleName().equals(UserRoleConstants.ADMIN) && !user.getUserRole().getRoleName().equals(UserRoleConstants.SUPERADMIN) 
					&& !StringFunction.isEmpty(user.getDivisi())) {
				list.add(criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(QuizEntity.Constant.DIVISI_FIELD)),
						SystemConstant.WILDCARD + user.getDivisi().toLowerCase() + SystemConstant.WILDCARD));
				list.add(criteriaBuilder.equal(root.<String>get(QuizEntity.Constant.PUBLISH_FIELD),
						ValidFlag.TRUE_BOOL));
			}	

			list.add(criteriaBuilder.greaterThan(root.get(QuizEntity.Constant.END_DATE_FIELD), new Date()));

			return criteriaBuilder.and(list.toArray(new Predicate[] {}));
		};

		Page<QuizEntity> paging = quizRepo.findAll(spec, pageable);

		paging.getContent().stream().map(quiz -> {
			quiz.setUrlPreview(UploadConstants.URL_PREVIEW.concat(OpenFileConstant.OPEN_CONTROLLER)
					.concat(QuizConstant.PREVIEW_FILE_ADDR).concat("?name="));
			if(quiz.getPublish() && DateTimeFunction.getTimeExpired(quiz.getStartDate())) {
				quiz.setAlreadyStart(true);
			}
			quiz.setStartDateDisplay(DateTimeFunction.getDatetimeFormatDisplay(quiz.getStartDate()));
			quiz.setEndDateDisplay(DateTimeFunction.getDatetimeFormatDisplay(quiz.getEndDate()));
			return quiz;
		}).collect(Collectors.toList());

		CommonResponsePaging<QuizEntity> restResponse = comGen
				.generateCommonResponsePaging(new CommonPaging<>(paging));

		ObjectWriter writer = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(QuizEntity.Constant.JSON_FILTER),
				new JsonFilter(QuizEntity.Constant.JSON_FILTER, QuizEntity.Constant.QUESTION_FIELD),
				new JsonFilter(QuestionEntity.Constant.JSON_FILTER));

		return writer.writeValueAsString(restResponse);
	}

	@Override
	public String searchResultQuiz(ResultQuizSearchRequest request) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		UserEntity users = new UserEntity();
		BeanUtils.copyProperties(principal, users);
		UserEntity user = userRepo.findByUsername(users.getUsername());

		Pageable pageable = pageUtil.generateDefaultPageRequest(request.getPage(),
				new Sort(Sort.Direction.ASC, QuizEntity.Constant.NAME_FIELD));

		Specification<QuizEntity> spec = (root, query, criteriaBuilder) -> {
			List<Predicate> list = new ArrayList<>();
			list.add(criteriaBuilder.equal(root.<String>get(QuizEntity.Constant.VALID_FIELD),
					ValidFlag.VALID));

			if (!user.getUserRole().getRoleName().equals(UserRoleConstants.ADMIN) && !user.getUserRole().getRoleName().equals(UserRoleConstants.SUPERADMIN) 
					&& !StringFunction.isEmpty(user.getDivisi())) {
				list.add(criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(QuizEntity.Constant.DIVISI_FIELD)),
						SystemConstant.WILDCARD + user.getDivisi().toLowerCase() + SystemConstant.WILDCARD));
				list.add(criteriaBuilder.equal(root.<String>get(QuizEntity.Constant.PUBLISH_FIELD),
						ValidFlag.TRUE_BOOL));
			}

			list.add(criteriaBuilder.lessThan(root.get(QuizEntity.Constant.END_DATE_FIELD), new Date()));

			return criteriaBuilder.and(list.toArray(new Predicate[] {}));
		};

		Page<QuizEntity> paging = quizRepo.findAll(spec, pageable);

		if (!user.getUserRole().getRoleName().equals(UserRoleConstants.ADMIN) && !user.getUserRole().getRoleName().equals(UserRoleConstants.SUPERADMIN)) {
			paging.getContent().stream().map(quiz -> {
				ResultQuizEntity resultQuiz = resultQuizRepo.findByUsernameAndQuiz(users.getUsername(), quiz.getId());
				quiz.setScore(resultQuiz.getScore());
				return quiz;
			}).collect(Collectors.toList());
		}else {
			paging.getContent().stream().map(quiz -> {
				quiz.setStartDateDisplay(DateTimeFunction.getDatetimeFormatDisplay(quiz.getStartDate()));
				quiz.setEndDateDisplay(DateTimeFunction.getDatetimeFormatDisplay(quiz.getEndDate()));
				return quiz;
			}).collect(Collectors.toList());
		}

		CommonResponsePaging<QuizEntity> restResponse = comGen
				.generateCommonResponsePaging(new CommonPaging<>(paging));

		ObjectWriter writer = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(QuizEntity.Constant.JSON_FILTER),
				new JsonFilter(QuizEntity.Constant.JSON_FILTER, QuizEntity.Constant.QUESTION_FIELD),
				new JsonFilter(QuestionEntity.Constant.JSON_FILTER));

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
		QuizEntity materi = quizRepo.findById(id).orElseThrow(() -> new UserException("400", "Quiz not found"));
		materi.setStartDate(DateTimeFunction.getDatePlus7Hour(materi.getStartDate()));
		materi.setEndDate(DateTimeFunction.getDatePlus7Hour(materi.getEndDate()));

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
			request.setNameFileJson(toUpdate.getNameFileJson());

			if(!request.getName().equals(toUpdate.getName())) {
				request.setNameFileJson(this.changeNameFile(toUpdate.getNameFile(), request.getName()));
			}

			request.setStartDate(DateTimeFunction.getDateMinus7Hour(request.getStartDate()));
			request.setEndDate(DateTimeFunction.getDateMinus7Hour(request.getEndDate()));
			
			if(DateTimeFunction.getStartGreaterThanEnd(request.getStartDate(), request.getEndDate())) {
				throw new UserException("400", "Start Date cannot Greater than End Date !");
			}
			
			BeanUtils.copyProperties(request, toUpdate);


			toUpdate.setModifiedBy(users.getUsername());
			toUpdate.setModifiedDate(new Date());
		}else {
			toUpdate.setStartDate(DateTimeFunction.getDateMinus7Hour(toUpdate.getStartDate()));
			toUpdate.setEndDate(DateTimeFunction.getDateMinus7Hour(toUpdate.getEndDate()));

			if(DateTimeFunction.getStartGreaterThanEnd(request.getStartDate(), request.getEndDate())) {
				throw new UserException("400", "Start Date cannot Greater than End Date !");
			}
			
			toUpdate.setCreatedBy(users.getUsername());
			toUpdate.setCreatedDate(new Date());
		}

		quizRepo.save(toUpdate);

		CommonResponse<String> response = comGen.generateCommonResponse(SystemConstant.SUCCESS);
		return JsonUtil.generateDefaultJsonWriter().writeValueAsString(response);
	}

	public String changeNameFile(List<String> filename, String nameQuiz) throws JsonProcessingException {
		int i = 0;
		List<String> newName = new ArrayList<>();
		for(String file : filename) {
			try {
				Resource source = this.download(file);
				File files = source.getFile();
				String name = nameQuiz
						.concat("-")
						.concat(String.valueOf(i + 1))
						.concat(".")
						.concat(FilenameUtils.getExtension(files.getName()));

				storageService.changeName(files, name);
				newName.add(name);
			} catch (Exception e) {
				e.printStackTrace();
			}
			i++;
		}

		return JsonUtil.generateJson(newName);
	}

	@Override
	public String saveQuizWithQuestion(QuestionEntity request) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		UserEntity users = new UserEntity();
		BeanUtils.copyProperties(principal, users);

		if (StringFunction.isEmpty(request.getQuizId())) throw new UserException("400", "Quiz is Mandatory!");

		QuizEntity quiz = quizRepo.findById(request.getQuizId()).orElseThrow(() -> new UserException("400", "Quiz not Found!"));

		QuestionEntity qt = request;
		qt.setQuiz(quiz);
		if(request.getId() != null && !StringFunction.isEmpty(request.getId())) {
			qt.setModifiedBy(users.getUsername());
			qt.setModifiedDate(new Date());
		} else {
			qt.setCreatedBy(users.getUsername());
			qt.setCreatedDate(new Date());
		}

		questionRepo.save(qt);

		CommonResponse<String> response = comGen.generateCommonResponse(SystemConstant.SUCCESS);
		return JsonUtil.generateDefaultJsonWriter().writeValueAsString(response);
	}

	@Override
	public String startQuiz(String id) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		UserEntity users = new UserEntity();
		BeanUtils.copyProperties(principal, users);
		Random rand = new Random();

		QuizEntity quiz = quizRepo.findById(id).orElseThrow(() -> new UserException("400", "Quiz not found"));

		if(quiz.getPublish() && !DateTimeFunction.getTimeExpired(quiz.getStartDate())) {
			throw new UserException("400", "the quiz hasn't started yet !");
		}

		ResultQuizEntity resultQuiz = resultQuizRepo.findByUsernameAndQuiz(users.getUsername(), quiz.getId());
		List<QuestionEntity> questions;
		List<QuestionEntity> listQuestionFinal = new ArrayList<>();
		Map<String, String> mapQuestion = new HashMap<>();
		if(resultQuiz == null) {
			questions = questionRepo.findByQuiz(quiz);
			for(int i=0; i<quiz.getTotalQuiz(); i++) {
				int randomIndex = rand.nextInt(questions.size());

				mapQuestion.put(questions.get(randomIndex).getId(), " ");
				listQuestionFinal.add(questions.get(randomIndex));

				questions.remove(randomIndex);
			}
			resultQuiz = new ResultQuizEntity();
			Map<String, String> treeMapQuestion = new TreeMap<String, String>(mapQuestion);
			String mapQuesions = JsonUtil.generateJson(treeMapQuestion);
			resultQuiz.setQuestionAnswerJson(mapQuesions);
			resultQuiz.setQuestionAnswer(treeMapQuestion);
			resultQuiz.setQuiz(quiz.getId());
			resultQuiz.setUsername(users.getUsername());
			listQuestionFinal.sort(Comparator.comparing(QuestionEntity::getId));
			resultQuiz.setQuestions(listQuestionFinal);
			resultQuiz.setEndDateQuiz(quiz.getEndDate());

			resultQuizRepo.save(resultQuiz);
		} else {
			questions = this.getListQuestions(resultQuiz.getQuestionAnswer());
			resultQuiz.setQuestions(questions);
			resultQuiz.setEndDateQuiz(quiz.getEndDate());
		}

		CommonResponse<ResultQuizEntity> response = new CommonResponse<>(resultQuiz);
		ObjectWriter writter = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(ResultQuizEntity.Constant.JSON_FILTER),
				new JsonFilter(ResultQuizEntity.Constant.JSON_FILTER, ResultQuizEntity.Constant.QUESTION_ANSWER_JSON_FIELD, 
						ResultQuizEntity.Constant.QUESTION_ANSWER_FIELD),
				new JsonFilter(QuestionEntity.Constant.JSON_FILTER, QuestionEntity.Constant.QUIZ_FIELD, 
						QuestionEntity.Constant.ANSWER_FIELD, QuestionEntity.Constant.FINISH_FIELD));

		return writter.writeValueAsString(response);
	}

	@Override
	public String resultQuiz(QuizSearchRequest request) throws Exception{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		UserEntity users = new UserEntity();
		BeanUtils.copyProperties(principal, users);
		Pageable pageable = pageUtil.generateDefaultPageRequest(request.getPage(),
				new Sort(Sort.Direction.ASC, ResultQuizEntity.Constant.ID_FIELD));

		Specification<ResultQuizEntity> spec = (root, query, criteriaBuilder) -> {
			List<Predicate> list = new ArrayList<>();
			list.add(criteriaBuilder.equal(root.<String>get(ResultQuizEntity.Constant.USERNAME_FIELD),
					users.getUsername()));
			return criteriaBuilder.and(list.toArray(new Predicate[] {}));
		};

		Page<ResultQuizEntity> paging = resultQuizRepo.findAll(spec, pageable);

		paging.getContent().stream().map(resultQuiz -> {
			Optional<QuizEntity> quiz = quizRepo.findById(resultQuiz.getQuiz());
			QuizEntity quizEn = quiz.get();
			resultQuiz.setQuizName(quizEn.getName());
			resultQuiz.setEndDateQuiz(quizEn.getEndDate());
			try {
				resultQuiz.setQuestions(this.getListQuestions(resultQuiz.getQuestionAnswer()));
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return resultQuiz;
		}).collect(Collectors.toList());
		
		paging = PageableExecutionUtils.getPage(
	    		paging.getContent().stream().filter(quiz -> DateTimeFunction.getTimeExpired(quiz.getEndDateQuiz()))
				.collect(Collectors.toList()),
				pageable,
				paging::getTotalElements);

		CommonResponsePaging<ResultQuizEntity> restResponse = comGen
				.generateCommonResponsePaging(new CommonPaging<>(paging));

		ObjectWriter writer = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(ResultQuizEntity.Constant.JSON_FILTER),
				new JsonFilter(ResultQuizEntity.Constant.JSON_FILTER, ResultQuizEntity.Constant.QUESTION_ANSWER_JSON_FIELD, 
						ResultQuizEntity.Constant.QUESTION_ANSWER_FIELD, ResultQuizEntity.Constant.END_DATE_FIELD),
				new JsonFilter(QuestionEntity.Constant.JSON_FILTER, QuestionEntity.Constant.QUIZ_FIELD, 
						QuestionEntity.Constant.ANSWER_FIELD, QuestionEntity.Constant.FINISH_FIELD));

		return writer.writeValueAsString(restResponse);
	}

	@Override
	public String answerQuiz(QuestionEntity question) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		UserEntity users = new UserEntity();
		BeanUtils.copyProperties(principal, users);

		ResultQuizEntity resultQuiz = resultQuizRepo.findByUsernameAndQuiz(users.getUsername(), question.getQuizId());
		List<QuestionEntity> questions;
		Map<String, String> mapQuestion = new HashMap<>();

		if(resultQuiz != null) {
			mapQuestion = resultQuiz.getQuestionAnswer();
			mapQuestion.put(question.getId(), question.getAnswerUser());
			String mapQuesions = JsonUtil.generateJson(mapQuestion);
			resultQuiz.setQuestionAnswerJson(mapQuesions);

			resultQuiz.setScore((int)getScore(mapQuestion));

			resultQuizRepo.save(resultQuiz);

			questions = this.getListQuestions(mapQuestion);
			resultQuiz.setQuestions(questions);
		}

		CommonResponse<ResultQuizEntity> response = new CommonResponse<>(resultQuiz);
		ObjectWriter writter = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(ResultQuizEntity.Constant.JSON_FILTER),
				new JsonFilter(ResultQuizEntity.Constant.JSON_FILTER, ResultQuizEntity.Constant.QUESTION_ANSWER_JSON_FIELD, 
						ResultQuizEntity.Constant.QUESTION_ANSWER_FIELD),
				new JsonFilter(QuestionEntity.Constant.JSON_FILTER, QuestionEntity.Constant.QUIZ_FIELD, 
						QuestionEntity.Constant.ANSWER_FIELD, QuestionEntity.Constant.FINISH_FIELD));

		return writter.writeValueAsString(response);
	}

	public double getScore(Map<String, String> mapQuestion) {
		double finalScore = 0;
		int correct = 0;
		for(Map.Entry<String, String> entry : mapQuestion.entrySet()) {
			Optional<QuestionEntity> question = questionRepo.findById(entry.getKey());
			QuestionEntity questionn = question.get();
			if(questionn.getAnswer().equals(entry.getValue().trim())) {
				finalScore = finalScore + (100/mapQuestion.size());
				correct++;
			}
		}
		if(correct == mapQuestion.size()) {
			return 100;
		}else {
			return finalScore;
		}
	}

	public List<QuestionEntity> getListQuestions(Map<String, String> mapQuestion){

		ArrayList<String> listId = new ArrayList<String>(mapQuestion.keySet());

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QuestionEntity> query = cb.createQuery(QuestionEntity.class);
		Root<QuestionEntity> root = query.from(QuestionEntity.class);
		Expression<String> parentExpression = root.get(QuestionEntity.Constant.ID_FIELD);			
		Predicate parentPredicate = parentExpression.in(listId);
		query.select(root).where(parentPredicate).orderBy(cb.asc(root.get(QuestionEntity.Constant.ID_FIELD)));

		List<QuestionEntity> questions = em.createQuery(query).getResultList();
		for(QuestionEntity question : questions) {
			question.setAnswerUser(mapQuestion.get(question.getId().trim()));
		}

		return questions;
	}

	@Override
	public String publishQuiz(QuizEntity request) throws Exception {
		QuizEntity quiz = quizRepo.findById(request.getId()).orElseThrow(() -> new UserException("400", "Quiz not found"));
		if(DateTimeFunction.getTimeExpired(quiz.getStartDate())){
			throw new UserException("400", "Start date has passed, please edit start date !");
		}
		int size = questionRepo.findByQuiz(quiz).size();
		if(	quiz.getTotalQuiz() > size) {
			throw new UserException("400", "Cannot publish, question not equals or greater than set total quiz");
		}
		quiz.setPublish(true);
		quizRepo.save(quiz);

		sendNotificationQuiz(quiz);

		CommonResponse<String> response = comGen.generateCommonResponse(SystemConstant.SUCCESS);
		return JsonUtil.generateDefaultJsonWriter().writeValueAsString(response);
	}

	@Async
	public void sendNotificationQuiz(QuizEntity quiz) throws Exception{
		CriteriaBuilder critBuilder = em.getCriteriaBuilder();
		quiz.setDivisi(quiz.getDivisi().replace("[", "").replace("]", "").replace("\"", ""));
		String[] split = quiz.getDivisi().split(",");
		List<String> list = Arrays.asList(split);
		
		CriteriaQuery<UserEntity> query = critBuilder.createQuery(UserEntity.class);
		Root<UserEntity> root = query.from(UserEntity.class);
		List<Predicate> lstWhere = new ArrayList<Predicate>();
		javax.persistence.criteria.Expression<String> parentExpression = root.get(QuizEntity.Constant.DIVISI_FIELD);
		javax.persistence.criteria.Predicate parentPredicate = parentExpression.in(list);
		lstWhere.add(parentPredicate);
		lstWhere.add(critBuilder.equal(root.get(QuizEntity.Constant.VALID_FIELD), 
				ValidFlag.VALID));
		query.select(root).where(lstWhere.toArray(new Predicate[] {}));
		
		List<UserEntity> users = em.createQuery(query).getResultList();
		users.stream().filter(user -> user.getUserRole().getUserRoleCode() != "0" && user.getUserRole().getUserRoleCode() != "1");

		String dateFormat = "HH:mm dd-MMM-yyyy";
		String date = DateTimeFunction.date2String(quiz.getStartDate(), dateFormat).concat(" - ").concat(DateTimeFunction.date2String(quiz.getEndDate(), dateFormat));
		Optional<EmailEntity> email = emailRepo.findById("QUIZNOTIF");
		for(UserEntity user : users) {
			EmailUtils.sendEmail(user.getUsername(), String.format(email.get().getBodyMessage(), user.getName(), quiz.getName(), date), email.get().getSubject());
		}

	}

	@Override
	public String searchMateriQuestion(QuestionRequest request) throws Exception {
		Pageable pageable = pageUtil.generateDefaultPageRequest(request.getPage(),
				new Sort(Sort.Direction.DESC, QuestionEntity.Constant.CREATED_DATE_FIELD));

		if (StringFunction.isEmpty(request.getId()) || request.getId() == null) {
			throw new UserException("400", "Header Quiz doesn't found");
		}

		QuizEntity quiz = new QuizEntity();
		quiz.setId(request.getId());

		Specification<QuestionEntity> spec = (root, query, criteriaBuilder) -> {
			List<Predicate> list = new ArrayList<>();
			list.add(criteriaBuilder.equal(root.<String>get(QuestionEntity.Constant.VALID_FIELD),
					ValidFlag.VALID));
			list.add(criteriaBuilder.equal(root.<String>get(QuestionEntity.Constant.QUIZ_FIELD), quiz));

			return criteriaBuilder.and(list.toArray(new Predicate[] {}));
		};

		Page<QuestionEntity> paging = questionRepo.findAll(spec,pageable);

		CommonResponsePaging<QuestionEntity> restResponse = comGen
				.generateCommonResponsePaging(new CommonPaging<>(paging));

		ObjectWriter writer = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(QuestionEntity.Constant.JSON_FILTER),
				new JsonFilter(QuestionEntity.Constant.JSON_FILTER, QuestionEntity.Constant.QUIZ_FIELD),
				new JsonFilter(QuizEntity.Constant.JSON_FILTER));

		return writer.writeValueAsString(restResponse);
	}

	@Override
	public String detailResultQuiz(ResultQuizSearchRequest request) throws Exception {
		Pageable pageable = pageUtil.generateDefaultPageRequest(request.getPage(),
				new Sort(Sort.Direction.DESC, ResultQuizEntity.Constant.ID_FIELD));

		if (StringFunction.isEmpty(request.getQuizId()) || request.getQuizId() == null) {
			throw new UserException("400", "Quiz id doesn't found");
		}
		Specification<ResultQuizEntity> spec = (root, query, criteriaBuilder) -> {
			List<Predicate> list = new ArrayList<>();
			list.add(criteriaBuilder.equal(criteriaBuilder.lower(root.<String>get(ResultQuizEntity.Constant.QUIZ_FIELD)),
					request.getQuizId()));

			return criteriaBuilder.and(list.toArray(new Predicate[] {}));
		};

		Page<ResultQuizEntity> paging = resultQuizRepo.findAll(spec, pageable);

		paging.getContent().stream().map(resultQuiz -> {
			UserEntity user = userRepo.findByUsername(resultQuiz.getUsername());
			resultQuiz.setNameuser(user.getName());
			return resultQuiz;
		}).collect(Collectors.toList());

		CommonResponsePaging<ResultQuizEntity> restResponse = comGen
				.generateCommonResponsePaging(new CommonPaging<>(paging));

		ObjectWriter writer = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(ResultQuizEntity.Constant.JSON_FILTER),
				new JsonFilter(ResultQuizEntity.Constant.JSON_FILTER, ResultQuizEntity.Constant.USERNAME_FIELD));

		return writer.writeValueAsString(restResponse);
	}

	@Override
	public ByteArrayOutputStream downloadResulQuiz(String id) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintWriter printWriter = new PrintWriter(bos);
		List<ResultQuizEntity> resultQuiz = resultQuizRepo.findByQuiz(id);

		boolean printHeader = true;
		try (CSVPrinter printer = new CSVPrinter(printWriter, CSVFormat.RFC4180)) {
			if(printHeader) {
				printer.printRecord(buildHeader());
			}
			resultQuiz.forEach(data -> {
				List<String> record = new ArrayList<>();
				UserEntity user = userRepo.findByUsername(data.getUsername());
				record.add(user.getName());
				record.add(user.getDivisi());
				record.add(String.valueOf(data.getScore()));
				try {
					printer.printRecord(record);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}

		return bos;
	}

	public List<String> buildHeader(){
		List<String> header = new ArrayList<>();
		header.add("Name");
		header.add("Divisi");
		header.add("Score");

		return header;
	}

}
