package com.ece452.domain;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class MultiFileUploadForm  {

	private List<MultipartFile> files;
	// getter and setter methods

	public List<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}


}