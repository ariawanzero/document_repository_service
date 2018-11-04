package com.asdp.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFilter;

@Entity(name = "MateriQuiz")
@Table(name = "materi_quiz")
@JsonFilter(MateriQuizEntity.Constant.JSON_FILTER)
public class MateriQuizEntity extends AuditEntity implements Serializable {

	private static final long serialVersionUID = 2371860388537337695L;
	@Id
	@GeneratedValue(generator = "code-uuid")
	@GenericGenerator(name = "code-uuid", strategy = "uuid")
	private String id;
	private String name;
	@Transient
	private List<String> nameFile;
	@Transient
	private String urlPreview;
	private String nameFileJson;
	private Integer valid = 1;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUrlPreview() {
		return urlPreview;
	}
	public void setUrlPreview(String urlPreview) {
		this.urlPreview = urlPreview;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getNameFile() {
		return nameFile;
	}
	public void setNameFile(List<String> nameFile) {
		this.nameFile = nameFile;
	}
	
	public Integer getValid() {
		return valid;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	
	public String getNameFileJson() {
		return nameFileJson;
	}
	public void setNameFileJson(String nameFileJson) {
		this.nameFileJson = nameFileJson;
	}


	public static class Constant {
		private Constant() {}
		public static final String ID_FIELD = "id";
		public static final String NAME_FIELD = "name";
		public static final String NAME_FILE_FIELD = "nameFile";
		public static final String NAME_FILE_JSON_FIELD = "nameFileJson";
		public static final String VALID_FIELD = "valid";
		public static final String JSON_FILTER = "jsonFilterMateriQuiz";
	}
}
