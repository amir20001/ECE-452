package com.ece452.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletException;
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

import com.ece452.dao.PlaylistDao;
import com.ece452.domain.Playlist;

@Controller
@RequestMapping("/playlist")
public class PlaylistController {

	@Autowired
	PlaylistDao playlistDao;

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public void insert(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		String json = "";
		if (br != null) {
			json = br.readLine();
		}

		ObjectMapper mapper = new ObjectMapper();
		Playlist playlist = mapper.readValue(json, Playlist.class);
		response.setContentType("application/json");
		playlist = playlistDao.insert(playlist);
		// return room info with id
		mapper.writeValue(response.getOutputStream(), playlist);
	}
	
	@RequestMapping(value = "/get/{playlistId}", method = RequestMethod.GET)
	public void getPlayList(@PathVariable("playlistId") String playlistId,
			HttpServletResponse response) throws JsonGenerationException,
			JsonMappingException, IOException {

		Playlist playlist = playlistDao.getPlaylist(playlistId);
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), playlist);
	}
	
	
	
	@RequestMapping(value = "/getall", method = RequestMethod.GET)
	public void getAllPlaylists(@PathVariable("playlistId") String playlistId,
			HttpServletResponse response) throws JsonGenerationException,
			JsonMappingException, IOException {

		List<Playlist> playlists = playlistDao.getAllPlaylist();
		
		
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), playlists);
	}
	
	
	

}
