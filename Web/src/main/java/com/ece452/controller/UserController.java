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

import com.ece452.domain.Favourite;
import com.ece452.domain.Room;

@Controller
@RequestMapping("/user")
public class UserController {

	@RequestMapping(value = "/addfavourite", method = RequestMethod.POST)
	public void addFavourite(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("add favs hit");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		String json = "";
		if (br != null) {
			json = br.readLine();
		}
		System.out.println(json);
		// initiate jackson mapper
		ObjectMapper mapper = new ObjectMapper();

		Favourite room = mapper.readValue(json, Favourite.class);
		System.out.println(room.toString());

		response.getOutputStream().write(room.toString().getBytes());

	}

}
