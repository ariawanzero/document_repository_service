package com.asdp.entity;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.asdp.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Entity(name = "Quiz")
@Table(name = "quiz")
@JsonFilter(QuizEntity.Constant.JSON_FILTER)
public class QuizEntity extends AuditEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 806585984180948592L;
	@Id
	@GeneratedValue(generator = "code-uuid")
	@GenericGenerator(name = "code-uuid", strategy = "uuid")
	private String id;
	private String name;
	private String description;
	private String divisi;
	private Date startDate;
	private Date endDate;
	private Integer totalQuiz;
	private Integer passedScore;
	private Integer valid = 1;
	//if the quiz hasn't started yet, passQuiz will be 0. for flagging can edit or not after the quiz has been started.
	private Boolean publish = false;
	private String nameFileJson;
	
	@OneToMany(mappedBy = QuestionEntity.Constant.QUIZ_FIELD)
	private Set<QuestionEntity> question;
	
	@Transient
	private List<String> nameFile;
	
	@Transient
	private String urlPreview;

	@Transient
	private boolean alreadyStart = false;
	
	@Transient
	private Integer score;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getPassedScore() {
		return passedScore;
	}

	public void setPassedScore(Integer passedScore) {
		this.passedScore = passedScore;
	}

	public boolean isAlreadyStart() {
		return alreadyStart;
	}

	public void setAlreadyStart(boolean alreadyStart) {
		this.alreadyStart = alreadyStart;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	public Set<QuestionEntity> getQuestion() {
		return question;
	}

	public void setQuestion(Set<QuestionEntity> question) {
		this.question = question;
	}

	public Integer getTotalQuiz() {
		return totalQuiz;
	}

	public void setTotalQuiz(Integer totalQuiz) {
		this.totalQuiz = totalQuiz;
	}

	public String getNameFileJson() {
		return nameFileJson;
	}

	public void setNameFileJson(String nameFileJson) {
		this.nameFileJson = nameFileJson;
	}
	
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@SuppressWarnings("unchecked")
	public List<String> getNameFile() throws JsonParseException, JsonMappingException, IOException {
		if(getNameFileJson() != null) {
			this.nameFile = JsonUtil.parseJson(getNameFileJson(), List.class);
		}
		return nameFile;
	}

	public void setNameFile(List<String> nameFile) {
		this.nameFile = nameFile;
	}

	public String getUrlPreview() {
		return urlPreview;
	}

	public void setUrlPreview(String urlPreview) {
		this.urlPreview = urlPreview;
	}


	public String getDivisi() {
		return divisi;
	}

	public void setDivisi(String divisi) {
		this.divisi = divisi;
	}


	public Boolean getPublish() {
		return publish;
	}

	public void setPublish(Boolean publish) {
		this.publish = publish;
	}


	public static class Constant {
		private Constant() {}
		public static final String ID_FIELD = "id";
		public static final String NAME_FIELD = "name";
		public static final String DESCRIPTION_FIELD = "description";
		public static final String DIVISI_FIELD = "divisi";
		public static final String START_DATE_FIELD = "startDate";
		public static final String END_DATE_FIELD = "endDate";
		public static final String QUESTION_FIELD = "question";
		public static final String VALID_FIELD = "valid";
		public static final String TOTAL_QUIZ_FIELD = "totalQuiz";
		public static final String PUBLISH_FIELD = "publish";
		public static final String NAME_FILE_FIELD = "nameFile";
		public static final String NAME_FILE_JSON_FIELD = "nameFileJson";
		public static final String QUIZ_FIELD = "quiz";
		public static final String PASSED_SCORE_FIELD = "passedScore";
		public static final String JSON_FILTER = "jsonFilterQuiz";
	}
	
	

}
