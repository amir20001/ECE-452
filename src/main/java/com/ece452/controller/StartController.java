package com.ece452.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class StartController {



	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showLoginPage(HttpSession session) {
		//testDao.test();
		return new ModelAndView("welcome");
	}
	
	@RequestMapping(value ="logout",method = RequestMethod.GET)
	public ModelAndView logout(HttpSession session) {
		session.invalidate();
		return new ModelAndView("redirect:/");
	}
	
}
