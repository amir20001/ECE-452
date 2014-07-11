package com.ece452.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

import com.ece452.dao.SongDao;
import com.ece452.domain.MultiFileUploadForm;
import com.ece452.domain.Song;

@Controller
public class FileUploadController {
	public static String path = "C:\\songs";

	@Autowired
	SongDao songDao;

	@Autowired
	FileHelper fileHelper;

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public String displayForm() {
		return "file_upload_form";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(
			@ModelAttribute("uploadForm") MultiFileUploadForm uploadForm,
			Model map, HttpServletRequest request)
			throws IllegalStateException, IOException {
		List<MultipartFile> files = uploadForm.getFiles();
		List<String> fileNames = new ArrayList<String>();
	//	String ext;
		if (null != files && files.size() > 0) {
			for (MultipartFile multipartFile : files) {
				String fileName = multipartFile.getOriginalFilename();
				fileNames.add(fileName);
				// Handle file content - multipartFile.getInputStream()
				//ext = FilenameUtils.getExtension(fileName);

				if (multipartFile.getSize() != 0) {
					Metadata metadata = new Metadata();
					ContentHandler handler = new DefaultHandler();
					Parser parser = new Mp3Parser();
					ParseContext parseCtx = new ParseContext();
					String uuid = UUID.randomUUID().toString();
					// file size is greater than 0
					 Song song = new Song();
					// song.setFileName(fileName);
					// song.setUuid(UUID.randomUUID().toString());
					// String fullPath = path + File.separator + song.getUuid()
					// + "." + ext;
					File dest = File.createTempFile(uuid, ".mp3");

					dest.deleteOnExit();
					multipartFile.transferTo(dest);
					fileHelper.upload(dest, uuid);
					try {
						InputStream inputStream = new FileInputStream(dest);
						parser.parse(inputStream, handler, metadata, parseCtx);
						song.setTitle(metadata.get("title"));
						song.setArtist(metadata.get("xmpDM:artist"));
						song.setAlbum(metadata.get("xmpDM:genre"));
					} catch (Exception e) {
						e.printStackTrace();
					} 
					song.setPlaylistId(2);
					dest.delete();
					
					songDao.insert(song);
				}
			}
		}

		map.addAttribute("files", fileNames);
		return "file_upload_success";
	}
}
