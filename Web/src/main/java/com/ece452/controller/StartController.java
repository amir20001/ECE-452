package com.ece452.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ece452.dao.RoomDao;
import com.ece452.domain.Room;

@Controller
@RequestMapping("/")
public class StartController {

	@Autowired
	RoomDao roomDao;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showLoginPage(HttpSession session, Model model) {
		List<Room> allRooms = roomDao.getAllRooms();
		model.addAttribute("rooms", allRooms);
		return new ModelAndView("createRoom");
	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpSession session) {
		session.invalidate();
		return new ModelAndView("redirect:/");
	}

}
