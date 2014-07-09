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
import org.springframework.web.servlet.ModelAndView;

import com.ece452.dao.UserDao;
import com.ece452.domain.User;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserDao userDao;

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public void insertUser(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		String json = "";
		if (br != null) {
			json = br.readLine();
		}
		ObjectMapper mapper = new ObjectMapper();
		User user = mapper.readValue(json, User.class);
		if (userDao.getUser(user.getuserID()) == null) {
			// make sure user does not already exist
			userDao.inset(user);
		}

		mapper.writeValue(response.getOutputStream(), user);
	}

	@RequestMapping(value = "/get/{username}", method = RequestMethod.GET)
	public void getSong(@PathVariable("username") String username,
			HttpServletResponse response) throws JsonGenerationException,
			JsonMappingException, IOException {

		User user = userDao.getUser(username);
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), user);
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView viewUsers(HttpSession session, Model model) {
		List<User> users = userDao.getAllUsers();
		model.addAttribute("users", users);
		return new ModelAndView("userView");
	}

}
