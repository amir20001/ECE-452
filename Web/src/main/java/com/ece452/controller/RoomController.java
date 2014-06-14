package com.ece452.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ece452.domain.Room;

@Controller
@RequestMapping("/room")
public class RoomController {

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

		// 4. Set response type to JSON

		// response.setContentType("application/json");

		// 5. Add article to List<Article>

		// 6. Send List<Article> as JSON to client
		response.getOutputStream().write(room.toString().getBytes());

		// mapper.writeValue(response.getOutputStream(), persons);
	}

}
