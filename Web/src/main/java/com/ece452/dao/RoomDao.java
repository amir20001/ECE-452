package com.ece452.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ece452.domain.Room;
import com.ece452.mapper.RoomAndSubObjectMapper;
import com.ece452.mapper.RoomMapper;
import com.mysql.jdbc.Statement;

@Repository
public class RoomDao {

	private DataSource dataSource;

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.dataSource = dataSource;
	}

	public Room insert(Room room) {

		String sql = "INSERT INTO room (name,owner_user_id,listener_count,current_song_id,playlist_id) VALUES (?,?,?,?,?)";
		ResultSet generatedKeys = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, room.getName());
			statement.setString(2, room.getOwnerUserId());
			statement.setInt(3, room.getListenerCount());
			if (room.getCurrentSongId() == 0) {
				statement.setNull(4, Types.INTEGER);
			} else {
				statement.setInt(4, room.getCurrentSongId());
			}
			statement.setInt(5, room.getPlaylistId());
			System.out.println(statement.toString());
			statement.executeUpdate();

			generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				// get auto increment key
				room.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException(
						"Creating room failed, no generated key obtained.");
			}
			generatedKeys.close();
			statement.close();
			return room;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}

	}

	public Room getRoom(String id) {
		Room room = null;
		String sql = "SELECT * FROM room WHERE id = ?";

		try {
			room = jdbcTemplate.queryForObject(sql, new Object[] { id },
					new RoomMapper());
		} catch (Exception e) {
			// No room was found with the specified id, return null
			return null;
		}
		return room;
	}

	public List<Room> getAllRooms() {
		String sql = "SELECT * FROM room INNER JOIN `user` ON room.owner_user_id= `user`.user_id JOIN playlist ON room.playlist_id = playlist.id;";
		List<Room> rooms = new ArrayList<Room>();
		try {
			rooms = jdbcTemplate.query(sql, new RoomAndSubObjectMapper());
			return rooms;
		} catch (Exception e) {
			return rooms;
		}
	}

}
