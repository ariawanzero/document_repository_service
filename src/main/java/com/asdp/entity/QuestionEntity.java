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
	
	private String answerA;
	private String answerB;
	private String answerC;
	private String answerD;
	private String correctAnswer;
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
	public String getAnswerA() {
		return answerA;
	}
	public void setAnswerA(String answerA) {
		this.answerA = answerA;
	}
	public String getAnswerB() {
		return answerB;
	}
	public void setAnswerB(String answerB) {
		this.answerB = answerB;
	}
	public String getAnswerC() {
		return answerC;
	}
	public void setAnswerC(String answerC) {
		this.answerC = answerC;
	}
	public String getAnswerD() {
		return answerD;
	}
	public void setAnswerD(String answerD) {
		this.answerD = answerD;
	}
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
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
		public static final String ANSWER_A_FIELD = "answerA";
		public static final String ANSWER_B_FIELD = "answerB";
		public static final String ANSWER_C_FIELD = "answerC";
		public static final String ANSWER_D_FIELD = "answerD";
		public static final String CORRECT_ANSWER_FIELD = "correctAnswer";
		public static final String VALID_FIELD = "valid";
		public static final String JSON_FILTER = "jsonFilterQuestion";
	}
}
