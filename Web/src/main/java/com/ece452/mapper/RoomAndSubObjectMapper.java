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
			Playlist playlist = new Playlist();
			Song song = new Song();
			
			user.map("user_",resultSet);
			playlist.map("playlist_",resultSet);
			song.map("song_",resultSet);
			room.map(resultSet);
			
			room.setUser(user);
			room.setSong(song);
			room.setPlaylist(playlist);
		} catch (Exception e) {
			System.out.println(e);
		}
		return room;
	}
}
