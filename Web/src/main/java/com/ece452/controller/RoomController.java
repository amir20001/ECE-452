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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ece452.dao.RoomDao;
import com.ece452.dao.UserDao;
import com.ece452.domain.Room;
import com.ece452.domain.Sync;
import com.ece452.domain.User;
import com.ece452.util.Content;
import com.ece452.util.GcmHelper;

@Controller
@RequestMapping("/room")
public class RoomController {

	@Autowired
	RoomDao roomDao;

	@Autowired
	UserDao userDao;

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public void create(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		String json = "";
		if (br != null) {
			json = br.readLine();
		}

		ObjectMapper mapper = new ObjectMapper();
		Room room = mapper.readValue(json, Room.class);
		response.setContentType("application/json");
		room = roomDao.insert(room);
		// return room info with id
		mapper.writeValue(response.getOutputStream(), room);
	}

	@RequestMapping(value = "/get/{roomId}", method = RequestMethod.GET)
	public void getRoom(@PathVariable("roomId") int roomID,
			HttpServletResponse response) throws JsonGenerationException,
			JsonMappingException, IOException {

		Room room = roomDao.getRoom(roomID);
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), room);
	}

	@RequestMapping(value = "/getall", method = RequestMethod.GET)
	public void getAllRooms(HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {
		List<Room> allRooms = roomDao.getAllRooms();

		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), allRooms);

	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView showLoginPage(HttpSession session, Model model) {
		List<Room> rooms = roomDao.getAllRooms();
		model.addAttribute("rooms", rooms);
		return new ModelAndView("roomView");
	}
	
	@RequestMapping(value = "/getCurrentUsers/{roomId}", method = RequestMethod.GET)
	public void getUsersInRoom(@PathVariable("roomId") int roomID,
			HttpServletResponse response) throws JsonGenerationException,
			JsonMappingException, IOException {
		List<User> usersInRoom = userDao.getUsersInRoom(roomID);
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		mapper.writeValue(response.getOutputStream(), usersInRoom);
	}

	@RequestMapping(value = "createaroom", method = RequestMethod.POST)
	public ModelAndView posttome(HttpSession session, Model model,
			@RequestParam("roomID") String roomId,
			@RequestParam("room_genre") String valueOne) {

		System.out.println(roomId);
		System.out.println(valueOne);

		List<Room> allRooms = roomDao.getAllRooms();
		model.addAttribute("rooms", allRooms);
		return new ModelAndView("create_room_success");
	}

	@RequestMapping(value = "/join/{roomId}/{userId}", method = RequestMethod.POST)
	public void join(HttpServletResponse response,
			@PathVariable("userId") String userId,
			@PathVariable("roomId") int roomId) throws JsonGenerationException,
			JsonMappingException, IOException {

		
		userDao.updateRoom(userId, roomId);
		roomDao.updateListenerCount(true, roomId);
		User user = userDao.getUser(userId);
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), user);
	}

	@RequestMapping(value = "/leave/{roomId}/{userId}", method = RequestMethod.POST)
	public void leave(HttpServletResponse response,
			@PathVariable("userId") String userId,
			@PathVariable("roomId") int roomId) throws JsonGenerationException,
			JsonMappingException, IOException {
		User user = userDao.getUser(userId);
		if (user.getRoomId() > 0) {
			user.setRoomId(0);
			roomDao.updateListenerCount(false, roomId);
			userDao.updateRoom(userId, 0);
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), user);
	}
	
	@RequestMapping(value = "/updatecurrentsong/{roomId}/{songId}", method = RequestMethod.POST)
	public void updateCurrentSong(HttpServletResponse response,
			@PathVariable("roomId") int roomId,
			@PathVariable("songId") int songId) throws JsonGenerationException,
			JsonMappingException, IOException {
		roomDao.updateCurrentSong(songId, roomId);
	}

	@RequestMapping(value = "/sync", method = RequestMethod.POST)
	public void sync(HttpServletResponse response, HttpServletRequest request)
			throws JsonGenerationException, JsonMappingException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		String json = "";
		if (br != null) {
			json = br.readLine();
		}
		ObjectMapper mapper = new ObjectMapper();
		Sync sync = mapper.readValue(json, Sync.class);
		List<User> usersInRoom = userDao.getUsersInRoom(sync.getRoomId());
		Content content = new Content();
		content.setSync(sync);
		for (User user : usersInRoom) {
			content.addRegId(user.getGcmId());
		}
		if (usersInRoom.size() > 0) {
			GcmHelper.post(content);
		}
	}

	@RequestMapping(value = "/delete/{roomId}", method = RequestMethod.POST)
	public void delete(HttpServletResponse response,
			@PathVariable("roomId") int roomId) throws JsonGenerationException,
			JsonMappingException, IOException {
		List<User> usersInRoom = userDao.getUsersInRoom(roomId);
		Sync sync = new Sync();
		Content content = new Content();
		sync.setAction(Sync.kick);
		sync.setRoomId(roomId);
		content.setSync(sync);

		for (User user : usersInRoom) {
			content.addRegId(user.getGcmId());
		}

		userDao.updateAllRoomRefs(roomId);
		roomDao.delete(roomId);
		if (usersInRoom.size() > 0) {
			GcmHelper.post(content);
		}
	}

}
