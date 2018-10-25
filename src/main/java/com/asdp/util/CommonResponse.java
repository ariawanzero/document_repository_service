package com.asdp.util;

import java.util.List;

public class CommonResponse<T> {
	CommonStatus responseStatus;
	String requestId;
	T data;
	List<ValidationDetail> validationDetails;

	public CommonResponse() {
		this.responseStatus = new CommonStatus("00", "SUCCESS");
	}

	public CommonResponse(CommonStatus status) {
		this.responseStatus = status;
	}

	public CommonResponse(T data) {
		this();
		this.data = data;
	}

	public T getData() {
		return this.data;
	}

	public String getRequestId() {
		return this.requestId;
	}

	public CommonStatus getResponseStatus() {
		return this.responseStatus;
	}

	public List<ValidationDetail> getValidationDetails() {
		return this.validationDetails;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public void setResponseStatus(CommonStatus responseStatus) {
		this.responseStatus = responseStatus;
	}

	public void setValidationDetails(List<ValidationDetail> validationDetails) {
		this.validationDetails = validationDetails;
	}
}
