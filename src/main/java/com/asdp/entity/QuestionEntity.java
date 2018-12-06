package com.asdp.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFilter;

@Entity(name = "Question")
@Table(name = "question")
@JsonFilter(QuestionEntity.Constant.JSON_FILTER)
public class QuestionEntity extends AuditEntity implements Serializable {

	private static final long serialVersionUID = -8741378358910643061L;
	@Id
	@GeneratedValue(generator = "code-uuid")
	@GenericGenerator(name = "code-uuid", strategy = "uuid")
	private String id;
	private String question;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="quiz")
	private QuizEntity quiz;
	
	@Transient
	private String quizId;
	
	private String choiceA;
	private String choiceB;
	private String choiceC;
	private String choiceD;
	private String answer;
	private Integer valid = 1;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getChoiceA() {
		return choiceA;
	}
	public void setChoiceA(String choiceA) {
		this.choiceA = choiceA;
	}
	public String getChoiceB() {
		return choiceB;
	}
	public void setChoiceB(String choiceB) {
		this.choiceB = choiceB;
	}
	public String getChoiceC() {
		return choiceC;
	}
	public void setChoiceC(String choiceC) {
		this.choiceC = choiceC;
	}
	public String getChoiceD() {
		return choiceD;
	}
	public void setChoiceD(String choiceD) {
		this.choiceD = choiceD;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public QuizEntity getQuiz() {
		return quiz;
	}
	public void setQuiz(QuizEntity quiz) {
		this.quiz = quiz;
	}

	public Integer getValid() {
		return valid;
	}
	
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	public String getQuizId() {
		if(getQuiz() !=null ) {
			this.quizId = getQuiz().getId();
		}
		return quizId;
	}
	public void setQuizId(String quizId) {
		this.quizId = quizId;
	}

	public static class Constant {
		private Constant() {}
		public static final String ID_FIELD = "id";
		public static final String QUIZ_FIELD = "quiz";
		public static final String QUESTION_FIELD = "question";
		public static final String CHOICE_A_FIELD = "choiceA";
		public static final String CHOICE_B_FIELD = "choiceB";
		public static final String CHOICE_C_FIELD = "choiceC";
		public static final String CHOICE_D_FIELD = "choiceD";
		public static final String ANSWER_FIELD = "answer";
		public static final String CREATED_DATE_FIELD = "createdDate";
		public static final String VALID_FIELD = "valid";
		public static final String JSON_FILTER = "jsonFilterQuestion";
	}
}
