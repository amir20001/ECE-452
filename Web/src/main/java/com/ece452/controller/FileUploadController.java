package com.ece452.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.ece452.domain.FileUploadForm;


@Controller
//@RequestMapping("/song")
public class FileUploadController {
	
	

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public String displayForm() {
		return "file_upload_form";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("uploadForm") FileUploadForm uploadForm,
			Model map,  HttpServletRequest request) throws IllegalStateException, IOException {
		boolean first = true;
		List<MultipartFile> files = uploadForm.getFiles();
		List<String> fileNames = new ArrayList<String>();
		String ext;
		if (null != files && files.size() > 0) {
			for (MultipartFile multipartFile : files) {
				
				String fileName = multipartFile.getOriginalFilename();
				fileNames.add(fileName);
				// Handle file content - multipartFile.getInputStream()
				ext = FilenameUtils.getExtension(fileName);
		
				if (multipartFile.getSize() != 0) {
					//img.setName(UUID.randomUUID().toString());
					
				}
			}
		}

		map.addAttribute("files", fileNames);
		return "file_upload_success";
	}
}
