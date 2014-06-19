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

import com.mysql.jdbc.Statement;
import com.ece452.domain.Song;
import com.ece452.domain.User;
import com.ece452.mapper.SongMapper;
import com.ece452.mapper.UserMapper;

@Repository
public class SongDao {
	private DataSource dataSource;

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.dataSource = dataSource;
	}

	public Song insert(Song song) {
		String sql = "INSERT INTO song (file_name,uuid,room_id,title,artist,album,duration) VALUES (?,?,?,?,?,?,?)";
		ResultSet generatedKeys = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, song.getFileName());
			statement.setString(2, song.getUuid());
			statement.setInt(3, song.getRoomId());
			statement.setString(4, song.getTitle());
			statement.setString(5, song.getArtist());
			statement.setString(6, song.getAlbum());
			statement.setString(7, song.getDuration());
			statement.executeUpdate();

			generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				// get auto increment key
				song.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException(
						"Creating song failed, no generated key obtained.");
			}
			generatedKeys.close();
			statement.close();
			return song;
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

	public Song getSong(String id) {
		Song song = null;
		String sql = "SELECT * FROM song WHERE id= ?";
		try {
			song = jdbcTemplate.queryForObject(sql, new Object[] { id },
					new SongMapper());
		} catch (Exception e) {
			// No user was found with the specified id, return null
			return null;
		}
		return song;
	}

}
