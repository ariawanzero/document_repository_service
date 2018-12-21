package com.asdp.service;

import java.io.File;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
	void store(MultipartFile file, String name);
	Resource loadFile(String filename) throws Exception;
	void deleteAll();
	void init();
	void changeName(File file, String name);
}
