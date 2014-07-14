package com.ece452.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ece452.domain.Playlist;
import com.ece452.domain.Room;
import com.ece452.domain.Song;
import com.ece452.domain.User;

public class RoomAndSubObjectMapper implements RowMapper<Room> {

	@Override
	public Room mapRow(ResultSet resultSet, int line) throws SQLException {
		Room room = new Room();
		try {
			User user = new User();
			user.map(resultSet);
			room.setUser(user);
			Playlist playlist = new Playlist();
			playlist.map(resultSet);
			room.setPlaylist(playlist);
			Song song = new Song();
			song.map(resultSet);
			room.setSong(song);
			room.map(resultSet);
		} catch (Exception e) {
			System.out.println(e);
		}
		return room;
	}
}
