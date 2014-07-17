package com.ece452.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ece452.dao.PlaylistDao;
import com.ece452.domain.Playlist;

@Controller
@RequestMapping("/playlist")
public class PlaylistController {

	@Autowired
	PlaylistDao playlistDao;

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public void insert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
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
	public void getPlayList(@PathVariable("playlistId") int playlistId, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {

		Playlist playlist = playlistDao.getPlaylist(playlistId);
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), playlist);
	}

	@RequestMapping(value = "/getbyuser/{userId}", method = RequestMethod.GET)
	public void getPlayListByUser(@PathVariable("userId") String userId, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {

		List<Playlist> playlistsByUser = playlistDao.getPlaylistsByUser(userId);
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), playlistsByUser);
	}

	@RequestMapping(value = "/getall", method = RequestMethod.GET)
	public void getAllPlaylists(@PathVariable("playlistId") String playlistId, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {

		List<Playlist> playlists = playlistDao.getAllPlaylist();

		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), playlists);
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView showLoginPage(HttpSession session, Model model) {
		List<Playlist> playlists = playlistDao.getAllPlaylist();
		model.addAttribute("playlists", playlists);
		return new ModelAndView("playlistView");
	}
	
	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public ModelAndView showPlaylist(@PathVariable("id") int id,
			HttpSession session, Model model) {
		Playlist playlist = playlistDao.getPlaylist(id);
		model.addAttribute("playlist", playlist);
		return new ModelAndView("singlePlaylistView");
	}
	
	@RequestMapping(value = "/createlist", method = RequestMethod.POST)
	public ModelAndView makePlaylistPage(HttpSession session, Model model,
			@RequestParam("name") String name,
			@RequestParam("genre") String valueOne
			) {
		System.out.println(name);
		System.out.println(valueOne);
		List<Playlist> playlists = playlistDao.getAllPlaylist();
		model.addAttribute("playlists", playlists);
		return new ModelAndView("create_playlist_success");
	}


	@RequestMapping(value = "/delete/{playlistId}", method = RequestMethod.POST)
	public void delete(@PathVariable("playlistId") int playlistId, HttpServletResponse response) {
		playlistDao.delete(playlistId);
	}

}
