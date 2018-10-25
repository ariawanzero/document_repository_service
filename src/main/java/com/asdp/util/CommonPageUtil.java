package com.asdp.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

@Component
public class CommonPageUtil {
	
	public PageRequest generateDefaultPageRequest(int page) {
		int size = this.getDefaultPageSize();
		return PageRequest.of(page, size);
	}

	public PageRequest generateDefaultPageRequest(int page, Direction direction, String... properties) {
		int size = this.getDefaultPageSize();
		return PageRequest.of(page, size, direction, properties);
	}

	public PageRequest generateDefaultPageRequest(int page, Sort sort) {
		int size = this.getDefaultPageSize();
		return PageRequest.of(page, size, sort);
	}

	public int getDefaultPageSize() {
		String pageSize = "10";
		return pageSize != null ? Integer.parseInt(pageSize) : 10;
	}
}
