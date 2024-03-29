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
		String sql = "INSERT INTO room (name,owner_user_id,listener_count,current_song_id,playlist_id,song_position,song_is_playing,song_play_start_time) VALUES (?,?,?,?,?,?,?,?)";
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
			statement.setInt(6, room.getSongPosition());
			statement.setBoolean(7, room.isSongIsPlaying());
			statement.setLong(8, room.getSongPlayStartTime());
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

	public Room getRoomNoObjects(int id) {
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

	public Room getRoom(int id) {
		Room room = null;
		String sql = "SELECT room.*, user.first_name AS user_first_name, "
				+ "user.last_name AS user_last_name, user.user_id AS user_user_id, "
				+ "user.score AS user_score, user.room_id AS user_room_id, "
				+ "user.gcm_id AS user_gcm_id, playlist.id AS playlist_id , "
				+ "playlist.name AS playlist_name , playlist.genre AS playlist_genre, "
				+ "playlist.track_count AS playlist_track_count ,playlist.user_id AS "
				+ "playlist_user_id , song.id AS song_id, song.file_name AS "
				+ "song_file_name, song.uuid AS song_uuid,song.title AS song_title, "
				+ "song.album AS song_album, song.artist AS song_artist, song.duration "
				+ "AS song_duration, song.playlist_id AS song_playlist_id, "
				+ "song.net_score AS song_net_score, song.song_url AS song_song_url,"
				+ " song.song_uri AS song_song_uri, song.art_url AS song_art_url FROM "
				+ "room INNER JOIN `user` ON room.owner_user_id= `user`.user_id AND "
				+ "room.id =? JOIN playlist ON room.playlist_id = playlist.id JOIN "
				+ "song ON room.current_song_id = song.id;";

		try {
			room = jdbcTemplate.queryForObject(sql, new Object[] { id },
					new RoomAndSubObjectMapper());
		} catch (Exception e) {
			// No room was found with the specified id, return null
			return null;
		}
		return room;
	}

	public List<Room> getAllRooms() {
		String sql = "SELECT room.*, user.first_name AS user_first_name, "
				+ "user.last_name AS user_last_name, user.user_id AS "
				+ "user_user_id, user.score AS user_score, user.room_id AS"
				+ " user_room_id, user.gcm_id AS user_gcm_id, playlist.id AS"
				+ "	playlist_id , playlist.name AS playlist_name , playlist.genre AS"
				+ " playlist_genre, playlist.track_count AS playlist_track_count ,"
				+ " playlist.user_id AS playlist_user_id , song.id AS song_id, "
				+ "song.file_name AS song_file_name, song.uuid AS song_uuid,"
				+ "song.title AS song_title, song.album AS song_album, "
				+ "song.artist AS song_artist, song.duration AS song_duration, "
				+ "song.playlist_id AS song_playlist_id, song.net_score AS "
				+ "song_net_score, song.song_url AS song_song_url, song.song_uri "
				+ "AS song_song_uri, song.art_url AS song_art_url FROM room INNER "
				+ "JOIN `user` ON room.owner_user_id= `user`.user_id JOIN playlist "
				+ "ON room.playlist_id = playlist.id JOIN song ON room.current_song_id"
				+ " = song.id;";
		List<Room> rooms = new ArrayList<Room>();
		try {
			rooms = jdbcTemplate.query(sql, new RoomAndSubObjectMapper());
			return rooms;
		} catch (Exception e) {
			return rooms;
		}
	}

	public void updateListenerCount(boolean increase, int roomId) {
		String incSql = "UPDATE room SET listener_count=listener_count+1 WHERE id =?";
		String decSql = "UPDATE room SET listener_count=listener_count-1 WHERE id =?";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement;
			if (increase) {
				statement = conn.prepareStatement(incSql);
			} else {
				statement = conn.prepareStatement(decSql);
			}
			statement.setInt(1, roomId);
			statement.executeUpdate();
			statement.close();
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

	public void updateCurrentSong(int songId, int roomId) {
		String sql = "UPDATE room SET current_song_id=?, song_position=?, song_is_playing=? ,song_play_start_time=? WHERE id =?";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement;
			statement = conn.prepareStatement(sql);
			statement.setInt(1, songId);
			statement.setInt(2, 0);
			statement.setInt(3, 1);
			statement.setInt(4, (int)System.currentTimeMillis());
			statement.setInt(5, roomId); 
			statement.executeUpdate();
			statement.close();
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

	public void delete(int roomId) {
		String sql = "DELETE FROM room WHERE id  = ?;";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, roomId);
			statement.executeUpdate();
			statement.close();
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

	public void updateSongData(int roomId, int songPosition,
			boolean songIsPlaying, long songPlayStartTime) {
		String sql = "UPDATE room SET song_position=?,"
				+ " song_is_playing=?, song_play_start_time=?  WHERE id =?";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement;
			statement = conn.prepareStatement(sql);

			statement.setInt(1, songPosition);
			statement.setBoolean(2, songIsPlaying);
			statement.setLong(3, songPlayStartTime);
			statement.setInt(4, roomId);
			statement.executeUpdate();
			statement.close();
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

}
