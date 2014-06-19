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

import com.ece452.dao.RoomDao;
import com.ece452.domain.Room;

@Controller
@RequestMapping("/room")
public class RoomController {

	@Autowired
	RoomDao roomDao;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public void create(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 1. get received JSON data from request
		BufferedReader br = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		String json = "";
		if (br != null) {
			json = br.readLine();
		}

		// 2. initiate jackson mapper
		ObjectMapper mapper = new ObjectMapper();

		// 3. Convert received JSON to Article
		Room room = mapper.readValue(json, Room.class);

		response.setContentType("application/json");

		room = roomDao.insert(room);
		// return room info with id
		mapper.writeValue(response.getOutputStream(), room);
	}

	@RequestMapping(value = "/info/{roomId}", method = RequestMethod.GET)
	public void getDoctorView(@PathVariable("roomId") String roomID,
			HttpServletResponse response) throws JsonGenerationException,
			JsonMappingException, IOException {

		Room room = roomDao.getRoom(roomID);
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), room);
	}

	

}
