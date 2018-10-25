package com.asdp.util;

import java.util.HashMap;
import java.util.List;

import com.asdp.entity.ResponseMappingEntity;
import com.asdp.service.ResponseMappingDaoService;

public class ResponseMappingDBImpl implements ResponseMapping {
	private HashMap<String, String> mapResponse = new HashMap<String, String>();
	ResponseMappingDaoService service;

	public ResponseMappingDBImpl(ResponseMappingDaoService service) {
		this.service = service;
		this.reloadResponseMapping();
	}

	public void reloadResponseMapping() {
		HashMap<String, String> mapResponseTmp = new HashMap<String, String>();
		List<ResponseMappingEntity> lst = this.service.getAllResponseMapping();
		lst.forEach(resp -> {
			mapResponseTmp.put(resp.getResponseCode(), resp.getResponseDesc());
		});
		this.mapResponse = mapResponseTmp;
	}

	public CommonStatus generateCommonStatus(String responseCode, String responseDesc) {
		String mappedResponse = (String) this.mapResponse.get(responseCode);
		if (mappedResponse == null) {
			mappedResponse = responseDesc;
		}

		return new CommonStatus(responseCode, mappedResponse);
	}

	public void updateValidationDetail(ValidationDetail valDet) {
		String mappedResponse = (String) this.mapResponse.get(valDet.getValidationCode());
		if (mappedResponse != null) {
			valDet.setValidationDesc(mappedResponse);
		}

	}
}
