package com.ece452.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

import com.ece452.dao.SongDao;
import com.ece452.domain.Song;

@Controller
@RequestMapping("/song")
public class SongController {

	@Autowired
	SongDao songDao;

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

	@RequestMapping(value = "/info/{songId}", method = RequestMethod.GET)
	public void getDoctorView(@PathVariable("songId") String songId,
			HttpServletResponse response) throws JsonGenerationException,
			JsonMappingException, IOException {
		Song song = songDao.getSong(songId);
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), song);
	}
}
