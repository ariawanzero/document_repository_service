package com.asdp.util;

public class JsonFilter {
	private String entity;
	private String[] fieldsException;

	public JsonFilter(String entity, String... fieldsException) {
		this.entity = entity;
		this.fieldsException = fieldsException;
	}

	public String getEntity() {
		return this.entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String[] getFieldsException() {
		return this.fieldsException;
	}

	public void setFieldsException(String... fieldsException) {
		this.fieldsException = fieldsException;
	}
}
