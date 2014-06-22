package com.ece452.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import com.ece452.domain.Room;

public class RoomMapper implements RowMapper<Room> {

	@Override
	public Room mapRow(ResultSet resultSet, int line) throws SQLException {
		Room room = new Room();
		try {
			room.map(resultSet);
		} catch (Exception e) {

		}
		return room;
	}
}
