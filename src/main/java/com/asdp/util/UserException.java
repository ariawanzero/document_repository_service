package com.asdp.util;

public class UserException extends Exception {
	
	private static final long serialVersionUID = 1L;
	String errorCode;
	String errorDesc;

	public UserException(String errorCode, String errorDesc) {
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public String getErrorDesc() {
		return this.errorDesc;
	}

	public String getMessage() {
		return this.errorDesc;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
}
