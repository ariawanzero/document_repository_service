package com.asdp.util;

import java.util.List;

public class CommonResponsePaging<T> {
	CommonStatus responseStatus;
	String requestId;
	CommonPaging<T> paging;
	List<ValidationDetail> validationDetails;

	public CommonResponsePaging() {
		this.responseStatus = new CommonStatus("00", "SUCCESS");
	}

	public CommonResponsePaging(CommonPaging<T> paging) {
		this();
		this.paging = paging;
	}

	public CommonResponsePaging(CommonStatus status) {
		this.responseStatus = status;
	}

	public CommonPaging<T> getPaging() {
		return this.paging;
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

	public void setPaging(CommonPaging<T> paging) {
		this.paging = paging;
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
