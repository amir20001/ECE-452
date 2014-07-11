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

import com.ece452.domain.Song;
import com.ece452.mapper.SongMapper;
import com.mysql.jdbc.Statement;

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
		String sql = "INSERT INTO song (uuid,playlist_id,title,artist"
				+ ",album,duration,net_score,song_url,song_uri,art_url) VALUES (?,?,?,?,?,?,?,?,?,?)";

		ResultSet generatedKeys = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, song.getUuid());
			statement.setInt(2, song.getPlaylistId());
			statement.setString(3, song.getTitle());
			statement.setString(4, song.getArtist());
			statement.setString(5, song.getAlbum());
			statement.setString(6, song.getDuration());
			statement.setInt(7, song.getNetScore());
			statement.setString(8, song.getSongUrl());
			statement.setString(9, song.getSongUri());
			statement.setString(10, song.getArtUrl());

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

	public List<Song> insertMultiple(List<Song> songs) {
		String sql = "INSERT INTO song (uuid,playlist_id,title,artist,album,duration,net_score,song_url,song_uri,art_url) VALUES (?,?,?,?,?,?,?,?,?,?)";
		ResultSet generatedKeys = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < songs.size(); i++) {
				Song song = songs.get(i);
				statement.setString(1, song.getUuid());
				statement.setInt(2, song.getPlaylistId());
				statement.setString(3, song.getTitle());
				statement.setString(4, song.getArtist());
				statement.setString(5, song.getAlbum());
				statement.setString(6, song.getDuration());
				statement.setInt(7, song.getNetScore());
				statement.setString(8, song.getSongUrl());
				statement.setString(9, song.getSongUri());
				statement.setString(10, song.getArtUrl());
				
				statement.addBatch();
			}
			statement.executeBatch();
			generatedKeys = statement.getGeneratedKeys();
			int i = 0;
			while (generatedKeys.next()) {
				// get auto increment key
				Song song = songs.get(i);
				song.setId(generatedKeys.getInt(1));
				songs.set(i, song);
				i++;
			}

			if (i != songs.size()) {
				throw new SQLException(
						"Creating songs failed, not all keys generated.");
			}

			generatedKeys.close();
			statement.close();
			return songs;
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

	public Song getSong(int id) {
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

	public List<Song> getAllByPlaylist(int id) {
		String sql = "SELECT * FROM song WHERE playlist_id= ?;";
		List<Song> songs = new ArrayList<Song>();
		try {
			songs = jdbcTemplate.query(sql, new Object[] { id },
					new SongMapper());
			return songs;
		} catch (Exception e) {
			return songs;
		}
	}

	public Song update(Song song) {
		String sql = "UPDATE song SET  `uuid` = ?, title = ?, album = ?, artist = ?,"
				+ " duration = ?, playlist_id = ?, net_score = ? ,song_url = ?, song_uri = ?, art_url = ?"
				+ " WHERE id = ?;";
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);

			statement.setString(1, song.getUuid());
			statement.setString(2, song.getTitle());
			statement.setString(3, song.getAlbum());
			statement.setString(4, song.getArtist());
			statement.setString(5, song.getDuration());
			statement.setInt(6, song.getPlaylistId());
			statement.setInt(7, song.getNetScore());
			statement.setString(8, song.getSongUrl());
			statement.setString(9, song.getSongUri());
			statement.setString(10, song.getArtUrl());
			statement.setInt(11, song.getId());

			statement.executeUpdate();
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

}
