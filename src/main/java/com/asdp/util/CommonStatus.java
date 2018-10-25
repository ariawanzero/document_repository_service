package com.asdp.util;

public class CommonStatus {
	String responseCode;
	String responseDesc;

	public CommonStatus() {
	}

	public CommonStatus(String responseCode, String responseDesc) {
		this.responseCode = responseCode;
		this.responseDesc = responseDesc;
	}

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
