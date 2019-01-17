package com.asdp.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

import com.asdp.entity.DocumentEntity;
import com.asdp.entity.EmailEntity;
import com.asdp.entity.HistoryDocumentEntity;
import com.asdp.entity.QuizEntity;
import com.asdp.entity.UserEntity;
import com.asdp.repository.DocumentRepository;
import com.asdp.repository.EmailRepository;
import com.asdp.repository.HistoryDocumentRepository;
import com.asdp.repository.UserRepository;
import com.asdp.request.DocumentRequest;
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
import com.asdp.util.SystemConstant.StatusConstants;
import com.asdp.util.SystemConstant.UploadConstants;
import com.asdp.util.SystemConstant.UserRoleConstants;
import com.asdp.util.SystemConstant.ValidFlag;
import com.asdp.util.SystemRestConstant.OpenFileConstant;
import com.asdp.util.SystemRestConstant.QuizConstant;
import com.asdp.util.UserException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;

import liquibase.util.file.FilenameUtils;

public class DocumentServiceImpl implements DocumentService {

	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	@Autowired
	private CommonResponseGenerator comGen;

	@Autowired
	private DocumentRepository documentRepo;

	@Autowired
	private HistoryDocumentRepository historyDocumentRepo;

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
	public String saveDocumentFile(MultipartFile file, String id) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		UserEntity users = new UserEntity();
		BeanUtils.copyProperties(principal, users);

		DocumentEntity en = documentRepo.findById(id).orElseThrow(() -> new UserException("400", "Data not Exists"));
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

		DocumentEntity materi = en;
		materi.setNameFileJson(JsonUtil.generateJson(fileName));
		materi.setModifiedBy(users.getUsername());
		materi.setModifiedDate(new Date());
		documentRepo.save(materi);

		CommonResponse<String> response = comGen.generateCommonResponse(SystemConstant.SUCCESS);
		return JsonUtil.generateDefaultJsonWriter().writeValueAsString(response);
	}

	@Override
	public String saveDocument(DocumentEntity request) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		UserEntity users = new UserEntity();
		BeanUtils.copyProperties(principal, users);

		DocumentEntity toUpdate = request;
		if (request == null || StringFunction.isEmpty(request.getName())) {
			throw new UserException("400", "Document Name is mandatory !");
		}
		if (isExistDocumentByDocumentName(request.getName(), request.getId())) {
			throw new UserException("400", "Document with that Name already exists !");
		}

		if (StringFunction.isNotEmpty(request.getId())) {
			Optional<DocumentEntity> existUser = documentRepo.findById(request.getId());
			if (existUser == null) {
				throw new UserException("400", "Document not found !");
			} else {
				toUpdate = existUser.get();
			}
			request.setNameFileJson(toUpdate.getNameFileJson());

			if(!request.getName().equals(toUpdate.getName())) {
				request.setNameFileJson(this.changeNameFile(toUpdate.getNameFile(), request.getName()));
			}

			if(users.getUserRole().getRoleName().equals(UserRoleConstants.USER)) {
				toUpdate.setStatus(StatusConstants.PENDING);
			}else {
				toUpdate.setStatus(StatusConstants.ACTIVE);
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
			
			if(users.getUserRole().getRoleName().equals(UserRoleConstants.USER)) {
				toUpdate.setStatus(StatusConstants.PENDING);
			}else {
				toUpdate.setStatus(StatusConstants.ACTIVE);
				sendNotificationQuiz(toUpdate);
			}
			toUpdate.setCreatedBy(users.getUsername());
			toUpdate.setCreatedDate(new Date());
		}

		documentRepo.save(toUpdate);

		CommonResponse<String> response = comGen.generateCommonResponse(SystemConstant.SUCCESS);
		return JsonUtil.generateDefaultJsonWriter().writeValueAsString(response);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String findDocumentDetail(String id) throws Exception {
		DocumentEntity document = documentRepo.findById(id).orElseThrow(() -> new UserException("400", "Document not found"));
		document.setStartDate(DateTimeFunction.getDatePlus7Hour(document.getStartDate()));
		document.setEndDate(DateTimeFunction.getDatePlus7Hour(document.getEndDate()));

		if(document.getNameFileJson() != null) {
			document.setNameFile(JsonUtil.parseJson(document.getNameFileJson(), ArrayList.class));
		}
		document.setUrlPreview(UploadConstants.URL_PREVIEW.concat(OpenFileConstant.OPEN_CONTROLLER)
				.concat(QuizConstant.PREVIEW_FILE_ADDR).concat("?name="));

		CommonResponse<DocumentEntity> response = new CommonResponse<>(document);
		ObjectWriter writter = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(DocumentEntity.Constant.JSON_FILTER),
				new JsonFilter(DocumentEntity.Constant.JSON_FILTER, QuizEntity.Constant.NAME_FILE_JSON_FIELD));

		return writter.writeValueAsString(response);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String readDocumentDetail(String id) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		UserEntity users = new UserEntity();
		BeanUtils.copyProperties(principal, users);
		UserEntity user = userRepo.findByUsername(users.getUsername());
		
		DocumentEntity document = documentRepo.findById(id).orElseThrow(() -> new UserException("400", "Document not found"));
		document.setStartDate(DateTimeFunction.getDatePlus7Hour(document.getStartDate()));
		document.setEndDate(DateTimeFunction.getDatePlus7Hour(document.getEndDate()));

		HistoryDocumentEntity hisDocument = historyDocumentRepo.findByUsernameAndDocument(user.getUsername(), id);
		if(hisDocument==null) {
			hisDocument = new HistoryDocumentEntity();
			hisDocument.setUsername(user.getUsername());
			hisDocument.setDocument(id);
			hisDocument.setReadDocument(new Date());
		}
		document.setCountRead(historyDocumentRepo.countByDocument(id));
		
		if(document.getNameFileJson() != null) {
			document.setNameFile(JsonUtil.parseJson(document.getNameFileJson(), ArrayList.class));
		}
		document.setUrlPreview(UploadConstants.URL_PREVIEW.concat(OpenFileConstant.OPEN_CONTROLLER)
				.concat(QuizConstant.PREVIEW_FILE_ADDR).concat("?name="));

		CommonResponse<DocumentEntity> response = new CommonResponse<>(document);
		ObjectWriter writter = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(DocumentEntity.Constant.JSON_FILTER),
				new JsonFilter(DocumentEntity.Constant.JSON_FILTER, QuizEntity.Constant.NAME_FILE_JSON_FIELD));

		return writter.writeValueAsString(response);
	}

	@Override
	public String searchDocument(DocumentRequest request) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		UserEntity users = new UserEntity();
		BeanUtils.copyProperties(principal, users);
		UserEntity user = userRepo.findByUsername(users.getUsername());

		Pageable pageable = pageUtil.generateDefaultPageRequest(request.getPage(),
				new Sort(Sort.Direction.ASC, DocumentEntity.Constant.NAME_FIELD));

		Specification<DocumentEntity> spec = (root, query, criteriaBuilder) -> {
			List<Predicate> list = new ArrayList<>();

			if (!StringFunction.isEmpty(request.getName())) {
				list.add(criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(DocumentEntity.Constant.NAME_FIELD)),
						SystemConstant.WILDCARD + request.getName().toLowerCase() + SystemConstant.WILDCARD));
			}

			if (!StringFunction.isEmpty(request.getDivisi())) {
				list.add(criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(DocumentEntity.Constant.DIVISI_FIELD)),
						SystemConstant.WILDCARD + request.getDivisi().toLowerCase() + SystemConstant.WILDCARD));
			}
			
			if (!StringFunction.isEmpty(request.getType())) {
				list.add(criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(DocumentEntity.Constant.TYPE_FIELD)),
						SystemConstant.WILDCARD + request.getType().toLowerCase() + SystemConstant.WILDCARD));
			}

			if (user.getUserRole().getRoleName().equals(UserRoleConstants.ADMIN) && user.getUserRole().getRoleName().equals(UserRoleConstants.SUPERADMIN)) {
				if (!StringFunction.isEmpty(request.getStatus())) {
					list.add(criteriaBuilder.like(criteriaBuilder.lower(root.<String>get(DocumentEntity.Constant.STATUS_FIELD)),
							SystemConstant.WILDCARD + request.getStatus().toLowerCase() + SystemConstant.WILDCARD));
				}
			}else {
				list.add(criteriaBuilder.equal(root.<String>get(DocumentEntity.Constant.VALID_FIELD),
						ValidFlag.VALID));
				list.add(criteriaBuilder.greaterThan(root.get(DocumentEntity.Constant.END_DATE_FIELD), new Date()));
				list.add(criteriaBuilder.lessThan(root.get(DocumentEntity.Constant.START_DATE_FIELD), new Date()));
			}

			list.add(criteriaBuilder.greaterThan(root.get(QuizEntity.Constant.END_DATE_FIELD), new Date()));

			return criteriaBuilder.and(list.toArray(new Predicate[] {}));
		};

		Page<DocumentEntity> paging = documentRepo.findAll(spec, pageable);

		List<DocumentEntity> listExpired = new ArrayList<>();
		
		paging.getContent().stream().map(doc -> {
			doc.setUrlPreview(UploadConstants.URL_PREVIEW.concat(OpenFileConstant.OPEN_CONTROLLER)
					.concat(QuizConstant.PREVIEW_FILE_ADDR).concat("?name="));
			if(DateTimeFunction.getTimeExpired(doc.getEndDate())){
				doc.setValid(SystemConstant.ValidFlag.INVALID);
				doc.setStatus(StatusConstants.EXPIRED);
				listExpired.add(doc);
			}
			return doc;
		}).collect(Collectors.toList());
		
		if(listExpired.size() > 0) updateStatusInvalid(listExpired);

		CommonResponsePaging<DocumentEntity> restResponse = comGen
				.generateCommonResponsePaging(new CommonPaging<>(paging));

		ObjectWriter writer = JsonUtil.generateJsonWriterWithFilter(
				new JsonFilter(DocumentEntity.Constant.JSON_FILTER));

		return writer.writeValueAsString(restResponse);
	}

	@Override
	public String searchDocumentPending(String id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String findHistoryDocumentDetail(DocumentRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String approveDocument(String id) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		UserEntity users = new UserEntity();
		BeanUtils.copyProperties(principal, users);
		UserEntity user = userRepo.findByUsername(users.getUsername());
		
		DocumentEntity document = documentRepo.findById(id).orElseThrow(() -> new UserException("400", "Document not found"));
		document.setModifiedBy(user.getUsername());
		document.setModifiedDate(new Date());
		document.setStatus(StatusConstants.ACTIVE);
		
		documentRepo.save(document);
		
		sendNotificationQuiz(document);
		
		CommonResponse<String> response = comGen.generateCommonResponse(SystemConstant.SUCCESS);
		return JsonUtil.generateDefaultJsonWriter().writeValueAsString(response);
	}
	
	private boolean isExistDocumentByDocumentName(String name, String id) {
		Specification<DocumentEntity> spec = (root, query, criteriaBuilder) -> {
			List<Predicate> list = new ArrayList<>();
			list.add(criteriaBuilder.equal(root.<Integer>get(DocumentEntity.Constant.VALID_FIELD), SystemConstant.ValidFlag.VALID));
			list.add(criteriaBuilder.equal(criteriaBuilder.lower(root.<String>get(DocumentEntity.Constant.NAME_FIELD)),
					name.toLowerCase()));

			if(id != null) {
				list.add(criteriaBuilder.notEqual(root.<String>get(DocumentEntity.Constant.ID_FIELD),id.toLowerCase()));
			}
			return criteriaBuilder.and(list.toArray(new Predicate[] {}));
		};

		Long rowCount = documentRepo.count(spec);
		return (rowCount != null && rowCount > 0 ? true : false);
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
	
	public Resource download(String nameFile) throws Exception {
		return storageService.loadFile(nameFile);
	}
	
	@Async
	private void updateStatusInvalid(List<DocumentEntity> listDoc){
		documentRepo.saveAll(listDoc);
	}
	
	@Async
	public void sendNotificationQuiz(DocumentEntity document) throws Exception{
		CriteriaBuilder critBuilder = em.getCriteriaBuilder();
		document.setDivisi(document.getDivisi().replace("[", "").replace("]", "").replace("\"", ""));
		String[] split = document.getDivisi().split(",");
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
		String date = DateTimeFunction.date2String(document.getStartDate(), dateFormat).concat(" - ").concat(DateTimeFunction.date2String(document.getEndDate(), dateFormat));
		Optional<EmailEntity> email = emailRepo.findById("DOCUMENTNOTIF");
		for(UserEntity user : users) {
			EmailUtils.sendEmail(user.getUsername(), String.format(email.get().getBodyMessage(), user.getName(), document.getName(), date), email.get().getSubject());
		}

	}

}
