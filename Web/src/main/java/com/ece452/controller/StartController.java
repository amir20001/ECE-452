package com.ece452.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ece452.dao.RoomDao;
import com.ece452.domain.Room;
import com.ece452.util.Content;
import com.ece452.util.GcmHelper;

@Controller
@RequestMapping("/")
public class StartController {

	@Autowired
	RoomDao roomDao;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showLoginPage(HttpSession session, Model model) {
		List<Room> allRooms = roomDao.getAllRooms();
		model.addAttribute("rooms", allRooms);
		return new ModelAndView("create_playlist");
	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpSession session) {
		session.invalidate();
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = "ip", method = RequestMethod.GET)
	public void ip(HttpSession session,HttpServletResponse response) throws IOException {
		session.invalidate();
		
		response.getOutputStream().write(getHTML("http://instance-data/latest/meta-data/public-hostname").getBytes());
	}
	
	
	@RequestMapping(value = "gcmTest", method = RequestMethod.GET)
	public void gcmtest(HttpSession session,HttpServletResponse response) throws IOException {
		Content content = new Content();
		content.addRegId("APA91bGuIGu0gGULMopesi9VGDZUJTKzWUTtaFfNe8LMfYqBBYbiQySrRHfWk3LaNfS8PDM5T5VInMqvx7DqOAROfAHJoKJbTp9OST9l1-99WC7Vf85biwJT45WXBiHQu5tcovoaFGaFu5daeG1c9BwoeELxkoL0yHSy4AlIw7BRgrppToZtfrQ");
		content.createData("Test Title", "Test Message");
		content.createData("dry_run", "true");
		GcmHelper.post(content);
	}
	
	
	
	
	  public String getHTML(String urlToRead) {
	      URL url;
	      HttpURLConnection conn;
	      BufferedReader rd;
	      String line;
	      String result = "";
	      try {
	         url = new URL(urlToRead);
	         conn = (HttpURLConnection) url.openConnection();
	         conn.setRequestMethod("GET");
	         rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	         while ((line = rd.readLine()) != null) {
	            result += line;
	         }
	         rd.close();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      return result;
	   }

}
