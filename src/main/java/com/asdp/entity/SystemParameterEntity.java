package com.asdp.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFilter;

@Entity(name = "SystemParameter")
@Table(name = "sys_param")
@JsonFilter(SystemParameterEntity.Constant.JSON_FILTER)
public class SystemParameterEntity implements Serializable {

	private static final long serialVersionUID = -6523379053617179429L;
	@Id
	private String code;
	private String value;
	private String type;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}


	public static class Constant {
		private Constant() {}
		public static final String JSON_FILTER = "jsonFilterSystemParameter";
	}
	
	
}
