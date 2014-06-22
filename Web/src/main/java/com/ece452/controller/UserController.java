package com.ece452.controller;

import java.io.IOException;

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

import com.ece452.dao.UserDao;
import com.ece452.domain.User;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserDao userDao;

	@RequestMapping(value = "/addfavourite", method = RequestMethod.POST)
	public void addFavourite(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO
	}

	@RequestMapping(value = "/info/{username}", method = RequestMethod.GET)
	public void getDoctorView(@PathVariable("username") String username,
			HttpServletResponse response) throws JsonGenerationException,
			JsonMappingException, IOException {

		User user = userDao.getUser(username);
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), user);
	}

}
