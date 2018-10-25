package com.asdp.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "ResponseMapping")
@Table(name = "sys_response_mapping")
public class ResponseMappingEntity {
	@Id
	String responseCode;
	String responseDesc;

	public String getResponseCode() {
		return this.responseCode;
	}

	public String getResponseDesc() {
		return this.responseDesc;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public void setResponseDesc(String responseDesc) {
		this.responseDesc = responseDesc;
	}
}
