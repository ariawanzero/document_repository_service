package com.asdp.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFilter;

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
	private Integer valid = 1;
	//if the quiz hasn't started yet, passQuiz will be 0. for flagging can edit or not after the quiz has been started.
	private Integer passQuiz = 0;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="materi_quiz")
	private MateriQuizEntity materiQuiz;
	
	@OneToMany(mappedBy = QuestionEntity.Constant.QUIZ_FIELD)
	private Set<QuestionEntity> question;
	
	@Transient
	private List<QuestionEntity> questionList;
	
	@Transient
	private MateriQuizEntity materi;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getDivisi() {
		return divisi;
	}

	public void setDivisi(String divisi) {
		this.divisi = divisi;
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

	public MateriQuizEntity getMateriQuiz() {
		return materiQuiz;
	}

	public void setMateriQuiz(MateriQuizEntity materiQuiz) {
		this.materiQuiz = materiQuiz;
	}

	public Set<QuestionEntity> getQuestion() {
		return question;
	}

	public void setQuestion(Set<QuestionEntity> question) {
		this.question = question;
	}

	public List<QuestionEntity> getQuestionList() {
		if (getQuestion() != null) {
			this.questionList = getQuestion().stream()
					.filter(a -> a.getValid() == 1)
					.collect(Collectors.toList());
		}
		return questionList;
	}

	public void setQuestionList(List<QuestionEntity> questionList) {
		this.questionList = questionList;
	}
	
	public MateriQuizEntity getMateri() {
		if(getMateriQuiz() != null) {
			this.materi = getMateriQuiz();
		}
		return materi;
	}

	public void setMateri(MateriQuizEntity materi) {
		this.materi = materi;
	}

	public Integer getTotalQuiz() {
		return totalQuiz;
	}

	public void setTotalQuiz(Integer totalQuiz) {
		this.totalQuiz = totalQuiz;
	}

	public Integer getPassQuiz() {
		return passQuiz;
	}

	public void setPassQuiz(Integer passQuiz) {
		this.passQuiz = passQuiz;
	}

	public static class Constant {
		private Constant() {}
		public static final String ID_FIELD = "id";
		public static final String NAME_FIELD = "name";
		public static final String DESCRIPTION_FIELD = "description";
		public static final String DIVISI_FIELD = "divisi";
		public static final String START_DATE_FIELD = "startDate";
		public static final String END_DATE_FIELD = "endDate";
		public static final String MATERI_QUIZ = "materiQuiz";
		public static final String QUESTION_FIELD = "question";
		public static final String VALID_FIELD = "valid";
		public static final String TOTAL_QUIZ_FIELD = "totalQuiz";
		public static final String PASS_QUIZ_FIELD = "passQuiz";
		public static final String JSON_FILTER = "jsonFilterQuiz";
	}
	
	

}
