package com.ece452.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.ece452.dao.SongDao;
import com.ece452.domain.FileUploadForm;
import com.ece452.domain.Song;

@Controller
public class FileUploadController {
	public static String path = "C:\\songs";

	@Autowired
	SongDao songDao;

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public String displayForm() {
		return "file_upload_form";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("uploadForm") FileUploadForm uploadForm,
			Model map, HttpServletRequest request)
			throws IllegalStateException, IOException {
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
					// file size is greater than 0
					Song song = new Song();
					song.setFileName(fileName);
					song.setUuid(UUID.randomUUID().toString());
					String fullPath = path + File.separator + song.getUuid()
							+ "." + ext;
					File dest = new File(fullPath);
					multipartFile.transferTo(dest);
					songDao.insert(song);
				}
			}
		}

		map.addAttribute("files", fileNames);
		return "file_upload_success";
	}
}
