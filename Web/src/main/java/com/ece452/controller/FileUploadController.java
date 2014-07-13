package com.ece452.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.ece452.dao.SongDao;
import com.ece452.domain.MultiFileUploadForm;
import com.ece452.domain.Song;
import com.mpatric.mp3agic.Mp3File;

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
	public String save(@ModelAttribute("uploadForm") MultiFileUploadForm uploadForm, Model map,
			HttpServletRequest request) throws IllegalStateException, IOException {
		List<MultipartFile> files = uploadForm.getFiles();
		List<String> fileNames = new ArrayList<String>();
		// String ext;
		if (null != files && files.size() > 0) {
			for (MultipartFile multipartFile : files) {
				String fileName = multipartFile.getOriginalFilename();
				fileNames.add(fileName);
				// Handle file content - multipartFile.getInputStream()
				// ext = FilenameUtils.getExtension(fileName);

				if (multipartFile.getSize() != 0) {
					// file size is greater than 0
					Song song = new Song();
					song.setUuid(UUID.randomUUID().toString());

					File dest = File.createTempFile(song.getUuid(), ".mp3");
					multipartFile.transferTo(dest);
					String url = fileHelper.upload(dest, song.getUuid());
					song.setSongUrl(url);
					song.setPlaylistId(2);// TODO hook this up to something

					try {
						Mp3File mp3file = new Mp3File(dest.getAbsolutePath());
						song.setTitle(FileHelper.getTitle(mp3file));
						song.setAlbum(FileHelper.getAlbum(mp3file));
						song.setArtist(FileHelper.getArtist(mp3file));
						song.setDuration(FileHelper.secToMin(mp3file.getLengthInSeconds()));
						File albumArt = FileHelper.getAlbumArt(mp3file);
						if (albumArt != null) {
							String artUrl = fileHelper.upload(albumArt, albumArt.getName());
							song.setArtUrl(artUrl);
							albumArt.delete();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					dest.delete();
					songDao.insert(song);
				}
			}
		}

		map.addAttribute("files", fileNames);
		return "file_upload_success";
	}
}
