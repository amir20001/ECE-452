package com.ece452.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

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

import com.ece452.dao.FavouriteDao;
import com.ece452.dao.SongDao;
import com.ece452.domain.Favourite;
import com.ece452.domain.Song;

@Controller
@RequestMapping("/favourite")
public class FavouriteController {
	@Autowired
	FavouriteDao favouriteDao;

	@Autowired
	SongDao songDao;

	@RequestMapping(value = "/get/{userId}", method = RequestMethod.GET)
	public void getPlayList(@PathVariable("userId") String userId, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {

		List<Favourite> allFavourites = favouriteDao.getAllFavourites(userId);
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), allFavourites);
	}

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public void inserts(HttpServletRequest request, HttpServletResponse response) throws JsonGenerationException,
			JsonMappingException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String json = "";

		if (br != null) {
			json = br.readLine();
		}

		ObjectMapper mapper = new ObjectMapper();
		Favourite favourite = mapper.readValue(json, Favourite.class);
		favourite = favouriteDao.insert(favourite);

		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), favourite);
	}

	@RequestMapping(value = "/insert/{userId}/{songId}", method = RequestMethod.POST)
	public void insertsFromsong(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("userId") String userId, @PathVariable("songId") int songId) throws JsonGenerationException,
			JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Song song = songDao.getSong(songId);

		Favourite favourite = new Favourite();
		favourite.setAlbum(song.getAlbum());
		favourite.setArtist(song.getArtist());
		favourite.setTitle(song.getTitle());
		favourite.setDuration(song.getDuration());
		favourite.setUserId(userId);
		favourite = favouriteDao.insert(favourite);

		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), favourite);
	}

	@RequestMapping(value = "/delete/{favId}", method = RequestMethod.POST)
	public void delete(HttpServletRequest request, HttpServletResponse response, @PathVariable("favId") int favId)
			throws JsonGenerationException, JsonMappingException, IOException {
		favouriteDao.delete(favId);

	}

}
