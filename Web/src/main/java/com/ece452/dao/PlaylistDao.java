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

import com.ece452.domain.Playlist;
import com.ece452.mapper.PlaylistMapper;
import com.mysql.jdbc.Statement;

@Repository
public class PlaylistDao {

	private DataSource dataSource;

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.dataSource = dataSource;
	}

	public Playlist insert(Playlist playlist) {

		String sql = "INSERT INTO playlist (name,genre,track_count) VALUES (?,?,?)";
		ResultSet generatedKeys = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, playlist.getName());
			statement.setString(2, playlist.getGenre());
			statement.setInt(3, playlist.getTrackCount());
			statement.executeUpdate();

			generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				// get auto increment key
				playlist.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException(
						"Creating playlist failed, no generated key obtained.");
			}
			generatedKeys.close();
			statement.close();
			return playlist;
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

	public Playlist getPlaylist(String id) {
		Playlist playlist = null;
		String sql = "SELECT * FROM playlist WHERE id = ?";
		try {
			playlist = jdbcTemplate.queryForObject(sql, new Object[] { id },
					new PlaylistMapper());
		} catch (Exception e) {
			// No room was found with the specified id, return null
			return null;
		}
		return playlist;
	}

	public List<Playlist> getAllPlaylist() {
		String sql = "SELECT * FROM playlist;";
		List<Playlist> playlists = new ArrayList<Playlist>();
		try {
			playlists = jdbcTemplate.query(sql, new PlaylistMapper());
			return playlists;
		} catch (Exception e) {
			return playlists;
		}
	}

}
