package com.asdp.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class CommonResponseGenerator {
	@Autowired
	private RequestContext requestContext;
	@Autowired
	private ResponseMapping responseMapping;

	public <T> CommonResponse<T> generateCommonResponse(Class<T> clz) {
		CommonResponse<T> resp = new CommonResponse<T>();
		resp.setRequestId((String) this.requestContext.get("requestId"));
		return resp;
	}

	public <T> CommonResponse<T> generateCommonResponse(String responseCode, String responseDesc, Class<T> clz) {
		CommonResponse<T> resp = this.generateCommonResponse(clz);
		CommonStatus stat = this.responseMapping.generateCommonStatus(responseCode, responseDesc);
		resp.setResponseStatus(stat);
		return resp;
	}

	public <T> CommonResponse<T> generateCommonResponse(String responseCode, String responseDesc,
			List<ValidationDetail> lstValDet, Class<T> clz) {
		CommonResponse<T> resp = this.generateCommonResponse(responseCode, responseDesc, clz);
		if (lstValDet != null) {
			lstValDet.forEach((valDet) -> {
				if (valDet != null) {
					this.responseMapping.updateValidationDetail(valDet);
				}

			});
			resp.setValidationDetails(lstValDet);
		}

		return resp;
	}

	public <T> CommonResponse<T> generateCommonResponse(T t) {
		CommonResponse<T> resp = new CommonResponse<T>();
		resp.setRequestId((String) this.requestContext.get("requestId"));
		resp.setData(t);
		return resp;
	}

	public <T> CommonResponsePaging<T> generateCommonResponsePaging(Class<T> clz) {
		CommonResponsePaging<T> resp = new CommonResponsePaging<T>();
		resp.setRequestId((String) this.requestContext.get("requestId"));
		return resp;
	}

	public <T> CommonResponsePaging<T> generateCommonResponsePaging(String responseCode, String responseDesc,
			Class<T> clz) {
		CommonResponsePaging<T> resp = this.generateCommonResponsePaging(clz);
		CommonStatus stat = this.responseMapping.generateCommonStatus(responseCode, responseDesc);
		resp.setResponseStatus(stat);
		return resp;
	}

	public <T> CommonResponsePaging<T> generateCommonResponsePaging(CommonPaging<T> t) {
		CommonResponsePaging<T> resp = new CommonResponsePaging<T>();
		resp.setRequestId((String) this.requestContext.get("requestId"));
		resp.setPaging(t);
		return resp;
	}
}
