package com.ece452.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ece452.dao.FollowDao;
import com.ece452.domain.Follow;
import com.ece452.domain.User;

@Controller
@RequestMapping("/follow")
public class FollowController {

	@Autowired
	FollowDao followDao;

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public void insert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String json = "";
		if (br != null) {
			json = br.readLine();
		}
		ObjectMapper mapper = new ObjectMapper();
		Follow follow = mapper.readValue(json, Follow.class);
		follow = followDao.insert(follow);
		mapper.writeValue(response.getOutputStream(), follow);
	}

	@RequestMapping(value = "/get/following/{userId}", method = RequestMethod.POST)
	public void getFollowing(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("userId") String userId) throws ServletException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		List<User> allFollowing = followDao.getAllFollowing(userId);
		mapper.writeValue(response.getOutputStream(), allFollowing);
	}

	@RequestMapping(value = "/get/followed/{userId}", method = RequestMethod.POST)
	public void getFollowed(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("userId") String userId) throws ServletException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		List<User> allFollowed = followDao.getAllFollowed(userId);
		mapper.writeValue(response.getOutputStream(), allFollowed);
	}

}
