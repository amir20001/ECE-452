package com.ece452.controller;

import java.io.IOException;
import java.util.List;

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
import com.ece452.domain.Favourite;

@Controller
@RequestMapping("/favourite")
public class FavouriteController {
	@Autowired
	FavouriteDao favouriteDao;

	@RequestMapping(value = "/get/{userid}", method = RequestMethod.GET)
	public void getPlayList(@PathVariable("userid") String userId,
			HttpServletResponse response) throws JsonGenerationException,
			JsonMappingException, IOException {

		List<Favourite> allFavourites = favouriteDao.getAllFavourites(userId);
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), allFavourites);
	}
	
}
