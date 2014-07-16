package com.ece452.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
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
	
	@RequestMapping(value = "/view/{roomId}", method = RequestMethod.GET)
	public ModelAndView showRoom(@PathVariable("roomId") int roomID,
			HttpSession session, Model model) {
		Room room = roomDao.getRoom(roomID);
		model.addAttribute("rooms", room);
		return new ModelAndView("singleRoomView");
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
		Room room = roomDao.getRoomNoObjects(roomId);
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), room);
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
		Date date = new Date();
		date.getTime();
		roomDao.updateCurrentSong(songId, roomId);
		roomDao.updateSongData(roomId, 0, true, date.getTime());
	}

	@RequestMapping(value = "/delete/{roomId}", method = RequestMethod.POST)
	public void delete(HttpServletResponse response,
			@PathVariable("roomId") int roomId) throws JsonGenerationException,
			JsonMappingException, IOException {
		List<User> usersInRoom = userDao.getUsersInRoom(roomId);
		Sync sync = new Sync();
		Content content = new Content();
		sync.setAction(Sync.kick);
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

	@RequestMapping(value = "/pause/{roomId}/{position}", method = RequestMethod.POST)
	public void hostPause(HttpServletResponse response,
			@PathVariable("roomId") int roomId,
			@PathVariable("position") int position)
			throws JsonGenerationException, JsonMappingException, IOException {
		Date date = new Date();
		Sync sync = new Sync();
		Content content = new Content();

		roomDao.updateSongData(roomId, position, false, date.getTime());
		Room room = roomDao.getRoom(roomId);
		List<String> gcmInRoom = userDao.getGcmInRoom(roomId);
		sync.setRoom(room);
		sync.setAction(Sync.roompause);
		for (String gcmId : gcmInRoom) {
			content.addRegId(gcmId);
		}
		content.setData(sync);
		GcmHelper.post(content);
	}
	
	
	@RequestMapping(value = "play/{roomId}", method = RequestMethod.POST)
	public void hostPlay(HttpServletResponse response,
			@PathVariable("roomId") int roomId)
			throws JsonGenerationException, JsonMappingException, IOException {
		Date date = new Date();
		Sync sync = new Sync();
		Content content = new Content();
		Room room = roomDao.getRoomNoObjects(roomId);
		roomDao.updateSongData(roomId, room.getSongPosition(), true, date.getTime());

		List<String> gcmInRoom = userDao.getGcmInRoom(roomId);
		sync.setRoom(room);
		sync.setAction(Sync.roomplay);
		for (String gcmId : gcmInRoom) {
			content.addRegId(gcmId);
		}
		content.setData(sync);
		GcmHelper.post(content);
	}

}
