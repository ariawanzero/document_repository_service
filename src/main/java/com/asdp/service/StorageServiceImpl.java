package com.asdp.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.asdp.util.SystemConstant.UploadConstants;
import com.asdp.util.UserException;

public class StorageServiceImpl implements StorageService {
	
	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	private final Path rootLocation = Paths.get(UploadConstants.PATH);

	@Override
	public void store(MultipartFile file, String name) {
		try {
			Files.copy(file.getInputStream(), this.rootLocation.resolve(name));
		} catch (Exception e) {
			throw new RuntimeException("FAIL!");
		}
	}
	
	@Override
	public void changeName(File file, String name) {
		try {
			file.renameTo(new File(this.rootLocation.resolve(name).toString()));
		} catch (Exception e) {
			throw new RuntimeException("FAIL!");
		}
	}

	@Override
	public Resource loadFile(String filename) throws UserException {
		try {
			Path file = rootLocation.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new UserException("400", "File not found !");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("General Error");
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectory(rootLocation);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize storage!");
		}
	}

}
