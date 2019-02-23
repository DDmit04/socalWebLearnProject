package com.web.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.data.Post;

@Service
public class FileService {
	
	@Value("${upload.path}")
	private String uploadPath;
	
	public void uploadFile(MultipartFile file, Post post) throws IllegalStateException, IOException {
		if (file != null && !file.getOriginalFilename().isEmpty()) {
			System.out.println("file");
			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdir();
			}
			String uuidFile = UUID.randomUUID().toString();
			String resultFilename = uuidFile + "." + file.getOriginalFilename();
			file.transferTo(new File(uploadPath + "/" + resultFilename));
			post.setFilename(resultFilename);
		}
	}

}
