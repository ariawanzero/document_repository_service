package com.asdp.util;

public interface ResponseMapping {
	void reloadResponseMapping() throws UserException;

	CommonStatus generateCommonStatus(String arg0, String arg1);

	void updateValidationDetail(ValidationDetail arg0);
}
