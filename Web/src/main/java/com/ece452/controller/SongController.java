package com.ece452.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.ece452.dao.SongDao;
import com.ece452.domain.FileUploadForm;
import com.ece452.domain.Song;
import com.mpatric.mp3agic.Mp3File;

@Controller
@RequestMapping("/song")
public class SongController {

	@Autowired
	SongDao songDao;

	@Autowired
	FileHelper fileHelper;

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public void create(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));

		String json = "";
		if (br != null) {
			json = br.readLine();
		}

		ObjectMapper mapper = new ObjectMapper();
		Song song = mapper.readValue(json, Song.class);
		response.setContentType("application/json");
		song = songDao.insert(song);
		mapper.writeValue(response.getOutputStream(), song);

	}

	@RequestMapping(value = "/insertmultiple", method = RequestMethod.POST)
	public void createMultiple(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));

		String json = "";
		if (br != null) {
			json = br.readLine();
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			JSONArray songs = new JSONArray(json);
			Song song = null;
			List<Song> songList = new ArrayList<Song>();
			for (int i = 0; i < songs.length(); i++) {
				song = mapper.readValue(songs.getString(i), Song.class);
				songList.add(song);
			}

			response.setContentType("application/json");
			songList = songDao.insertMultiple(songList);

			for (int i = 0; i < songList.size(); i++) {
				song = songList.get(i);
				mapper.writeValue(response.getOutputStream(), song);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/get/{songId}", method = RequestMethod.GET)
	public void getSong(@PathVariable("songId") int songId, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {
		Song song = songDao.getSong(songId);
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), song);
	}

	@RequestMapping(value = "/getforplaylist/{playlistId}", method = RequestMethod.GET)
	public void getSongsForPlaylist(@PathVariable("playlistId") int playlistId, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {

		List<Song> songs = songDao.getAllByPlaylist(playlistId);

		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), songs);
	}

	@RequestMapping(value = "upload/{songId}", method = RequestMethod.POST)
	public void upload(@PathVariable("songId") int songId, HttpServletResponse response, HttpServletRequest request,
			@RequestParam("file") FileUploadForm file) throws IOException {
		Song song = songDao.getSong(songId);

		String uuid = UUID.randomUUID().toString();
		File dest = File.createTempFile(uuid, ".mp3");
		file.getFile().transferTo(dest);
		String url = fileHelper.upload(dest, uuid);
		song.setSongUrl(url);
		song.setUuid(uuid);

		try {
			Mp3File mp3file = new Mp3File(dest.getAbsolutePath());
			song.setTitle(FileHelper.getTitle(mp3file));
			song.setAlbum(FileHelper.getAlbum(mp3file));
			song.setArtist(FileHelper.getArtist(mp3file));
			song.setDuration(FileHelper.secToMin(mp3file.getLengthInSeconds()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		songDao.update(song);

		dest.delete();

	}

	@RequestMapping(value = "upload2/{songId}", method = RequestMethod.POST)
	public void upload2(@PathVariable("songId") int songId, HttpServletResponse response, HttpServletRequest request)
			throws IOException {
		Song song = songDao.getSong(songId);
		ServletInputStream inputStream = request.getInputStream();
		OutputStream outputStream = null;
		System.out.println("in upload 2");
		try {
			int read = 0;
			byte[] bytes = new byte[1024];
			String uuid = UUID.randomUUID().toString();
			File dest = File.createTempFile(uuid, ".mp3");
			outputStream = new FileOutputStream(dest);

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			String url = fileHelper.upload(dest, uuid);
			song.setSongUrl(url);
			song.setUuid(uuid);
			// clean up
			outputStream.close();
			inputStream.close();
			dest.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

		songDao.update(song);

	}

}
