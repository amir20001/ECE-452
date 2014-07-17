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

import com.ece452.domain.Favourite;
import com.ece452.mapper.FavouriteMapper;
import com.mysql.jdbc.Statement;

@Repository
public class FavouriteDao {

	private DataSource dataSource;

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.dataSource = dataSource;
	}

	public Favourite insert(Favourite favourite) {

		String sql = "INSERT INTO favourite (user_id,song_id ) VALUES (?,?)";
		ResultSet generatedKeys = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);

			statement.setString(1, favourite.getUserId());
			statement.setInt(2, favourite.getId());

			statement.executeUpdate();

			generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				// get auto increment key
				favourite.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException(
						"Creating favs failed, no generated key obtained.");
			}
			generatedKeys.close();
			statement.close();
			return favourite;
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

	public List<Favourite> getAllFavourites(String userId) {
		String sql = "SELECT favourite.id AS favourite_id, favourite.song_id AS "
				+ "favourite_song_id,favourite.user_id AS favourite_user_id, "
				+ "song.* FROM favourite JOIN song ON favourite.song_id = "
				+ "song.id AND favourite.user_id = ?";
		List<Favourite> favourite = new ArrayList<Favourite>();
		try {
			favourite = jdbcTemplate.query(sql, new Object[] { userId },
					new FavouriteMapper());
			return favourite;
		} catch (Exception e) {
			return favourite;
		}
	}

	public void delete(String userId, int songId) {
		String sql = "DELETE FROM favourite WHERE user_id  = ? and song_id =?;";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			statement.setInt(2, songId);
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
