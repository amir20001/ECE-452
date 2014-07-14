package com.ece452.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.ece452.dao.PlaylistDao;
import com.ece452.dao.SongDao;
import com.ece452.dao.UserDao;
import com.ece452.domain.Playlist;
import com.ece452.domain.Song;
import com.ece452.domain.Sync;
import com.ece452.domain.User;
import com.ece452.util.Content;
import com.ece452.util.FileHelper;
import com.ece452.util.GcmHelper;
import com.mpatric.mp3agic.Mp3File;

@Controller
@RequestMapping("/song")
public class SongController {

	@Autowired
	SongDao songDao;

	@Autowired
	PlaylistDao playlistDao;

	@Autowired
	UserDao userDao;

	@Autowired
	FileHelper fileHelper;

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public void create(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(
				request.getInputStream()));

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
	public void createMultiple(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(
				request.getInputStream()));

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
	public void getSong(@PathVariable("songId") int songId,
			HttpServletResponse response) throws JsonGenerationException,
			JsonMappingException, IOException {
		Song song = songDao.getSong(songId);
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), song);
	}

	@RequestMapping(value = "/getforplaylist/{playlistId}", method = RequestMethod.GET)
	public void getSongsForPlaylist(@PathVariable("playlistId") int playlistId,
			HttpServletResponse response) throws JsonGenerationException,
			JsonMappingException, IOException {

		List<Song> songs = songDao.getAllByPlaylist(playlistId);

		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), songs);
	}

	@RequestMapping(value = "upload/{songId}", method = RequestMethod.POST)
	public void upload3(@PathVariable("songId") int songId,
			HttpServletResponse response, HttpServletRequest request,
			@RequestParam("file") MultipartFile file) throws IOException {
		System.out.println("in upload3 controller");
		Song song = songDao.getSong(songId);
		String uuid = UUID.randomUUID().toString();
		File dest = File.createTempFile(uuid, ".mp3");
		file.transferTo(dest);
		String url = fileHelper.upload(dest, uuid);
		song.setSongUrl(url);
		song.setUuid(uuid);

		try {
			Mp3File mp3file = new Mp3File(dest.getAbsolutePath());
			File albumArt = FileHelper.getAlbumArt(mp3file);
			if (albumArt != null) {
				String artUrl = fileHelper.upload(albumArt, albumArt.getName());
				song.setArtUrl(artUrl);
				albumArt.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		songDao.update(song);

		dest.delete();

	}

	@RequestMapping(value = "/delete/{songId}", method = RequestMethod.POST)
	public void delete(HttpServletRequest request,
			HttpServletResponse response, @PathVariable("songId") int songId) {
		Song song = songDao.getSong(songId);
		if (song != null) {
			String uuid = song.getUuid();
			if (!StringUtils.isEmpty(uuid)) {
				fileHelper.delete(uuid);
			}
		}

		try {
			Thread.sleep(5);
			song = songDao.getSong(songId);

			Sync sync = new Sync();
			sync.setAction(Sync.score);
			Content content = new Content();
			sync.setSongScore(song.getNetScore());
			sync.setSongId(songId);

			int playlistId = song.getPlaylistId();
			Playlist playlist = playlistDao.getPlaylist(playlistId);
			if (playlist != null) {
				if (!StringUtils.isEmpty(playlist.getUserId())) {
					User owner = userDao.getUser(playlist.getUserId());
					if (owner != null) {
						int roomId = owner.getRoomId();
						List<User> usersInRoom = userDao.getUsersInRoom(roomId);
						for (User user : usersInRoom) {
							content.addRegId(user.getGcmId());
						}
						sync.setRoomId(roomId);
					}
				}
			}

			content = sync.addToContent(content);
			GcmHelper.post(content);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

	}

	@RequestMapping(value = "/vote/{songId}/{value}", method = RequestMethod.POST)
	public void vote(HttpServletRequest request,
			HttpServletResponse response, @PathVariable("songId") int songId,
			@PathVariable("value") int value) {
		Song song = songDao.getSong(songId);
		String userId = null;
		if (song != null) {
			int playlistId = song.getPlaylistId();
			Playlist playlist = playlistDao.getPlaylist(playlistId);
			if (playlist != null) {
				userId = playlist.getUserId();
			}
		}

		if (value > 0) {
			songDao.vote(true, songId);
			if (userId != null) {
				userDao.updateScore(userId, 1);
			}
		} else {
			songDao.vote(false, songId);
			if (userId != null) {
				userDao.updateScore(userId, -1);
			}
		}
	}
}
