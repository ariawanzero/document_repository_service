package com.asdp.entity;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.asdp.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Entity(name = "ResultQuiz")
@Table(name = "result_quiz")
@JsonFilter(ResultQuizEntity.Constant.JSON_FILTER)
public class ResultQuizEntity implements Serializable {

	private static final long serialVersionUID = -4505382576017318421L;
	@Id
	@GeneratedValue(generator = "code-uuid")
	@GenericGenerator(name = "code-uuid", strategy = "uuid")
	private String id;
	private String username;
	private String quiz;
	private Integer score;
	
	@Transient
	private Map<String, String> questionAnswer;
	
	@Transient
	private List<QuestionEntity> questions;
	
	@Transient
	private String nameuser;
	
	@Transient
	private String quizName;
	
	private String questionAnswerJson;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQuizName() {
		return quizName;
	}
	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}
	public List<QuestionEntity> getQuestions() {
		return questions;
	}
	public void setQuestions(List<QuestionEntity> questions) {
		this.questions = questions;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getQuiz() {
		return quiz;
	}
	public void setQuiz(String quiz) {
		this.quiz = quiz;
	}
	@SuppressWarnings("unchecked")
	public Map<String, String> getQuestionAnswer() throws JsonParseException, JsonMappingException, IOException {
		if(getQuestionAnswerJson() != null) {
			this.questionAnswer = JsonUtil.parseJson(getQuestionAnswerJson(), Map.class);
		}
		return questionAnswer;
	}
	public void setQuestionAnswer(Map<String, String> questionAnswer) {
		this.questionAnswer = questionAnswer;
	}
	public String getQuestionAnswerJson() {
		return questionAnswerJson;
	}
	public void setQuestionAnswerJson(String questionAnswerJson) {
		this.questionAnswerJson = questionAnswerJson;
	}
	
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}

	public String getNameuser() {
		return nameuser;
	}
	public void setNameuser(String nameuser) {
		this.nameuser = nameuser;
	}



	public static class Constant {
		private Constant() {}
		public static final String ID_FIELD = "id";
		public static final String USERNAME_FIELD = "username";
		public static final String QUESTION_ANSWER_FIELD = "questionAnswer";
		public static final String QUESTION_ANSWER_JSON_FIELD = "questionAnswerJson";
		public static final String QUESTIONS_JSON_FIELD = "questions";
		public static final String QUIZ_FIELD = "quiz";
		public static final String SCORE_FIELD = "score";
		public static final String JSON_FILTER = "jsonFilterResultQuiz";
	}

}
