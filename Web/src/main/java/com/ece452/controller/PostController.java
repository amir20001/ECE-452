package com.ece452.controller;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.http.HttpRequest;
import org.apache.http.params.HttpParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ece452.dao.RoomDao;
import com.ece452.domain.Room;

@Controller
@RequestMapping("/test")
public class PostController {

	@Autowired
	RoomDao roomDao;

	@RequestMapping(value = "getjsp", method = RequestMethod.GET)
	public ModelAndView getjsp(HttpSession session, Model model) {
		return new ModelAndView("createRoom");
	}

	@RequestMapping(value = "postjsp", method = RequestMethod.POST)
	public ModelAndView posttome(HttpSession session, Model model,
			@RequestParam("roomID") String roomId,
			@RequestParam("room_genre") String valueOne) {

		System.out.println(roomId);
		System.out.println(valueOne);
		
		List<Room> allRooms = roomDao.getAllRooms();
		model.addAttribute("rooms", allRooms);
		return new ModelAndView("create_room_success");
	}
}
