package com.asdp.util;

public class ValidationDetail {
	private String validationCode;
	private String validationDesc;
	private String field;

	public ValidationDetail(String validationCode, String validationDesc, String field) {
		this.validationCode = validationCode;
		this.validationDesc = validationDesc;
		this.field = field;
	}

	public String getValidationCode() {
		return this.validationCode;
	}

	public void setValidationCode(String validationCode) {
		this.validationCode = validationCode;
	}

	public String getValidationDesc() {
		return this.validationDesc;
	}

	public void setValidationDesc(String validationDesc) {
		this.validationDesc = validationDesc;
	}

	public String getField() {
		return this.field;
	}

	public void setField(String field) {
		this.field = field;
	}
}
