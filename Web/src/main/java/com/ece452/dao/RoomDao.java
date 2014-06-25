package com.ece452.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ece452.domain.Room;
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

		String sql = "INSERT INTO room (name,owner_user_name,listener_count,current_song_id,playlist_id) VALUES (?,?,?,?,?)";
		ResultSet generatedKeys = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, room.getName());
			statement.setString(2, room.getOwnerUserName());
			statement.setInt(3, room.getListenerCount());
			statement.setInt(4, room.getCurrentSongID());
			statement.setInt(5, room.getPlaylistId());
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
		String sql = "SELECT * FROM room;";
		List<Room> rooms = new ArrayList<Room>();
		try {
			rooms = jdbcTemplate.query(sql, new RoomMapper());
			return rooms;
		} catch (Exception e) {
			return rooms;
		}
	}

}
