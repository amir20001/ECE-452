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
import com.ece452.domain.Sync;
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
		content.addRegId("APA91bFRDg_Qfqbg3X_GeoYuXlfjwszfVOUWAgSNqjKgjiT8RQ-nu7Kds-Ax5TlZ520XsA5X8MYdHhaEDe4fJatlTLbP9qSRGOBr5hvd13sd8OZT2VSi9I2f5Cto3LHBYtoPi8G46OSsqixK3jqCh1owWJ_LB0otxnKMrL3bW9SaSNrUDldmjXE");
		
		Sync sync = new Sync();
		sync.setAction("Sync");
		sync.setRoomId(1);
		sync.setSongId(1);
		sync.setPosition(20);
		
		content.setSync(sync);
		
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
