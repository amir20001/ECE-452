package com.ece452.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ece452.domain.Favourite;
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

		String sql = "INSERT INTO favourite (title,album,artist,user_id ) VALUES (?,?,?,?)";
		ResultSet generatedKeys = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, favourite.getTitle());
			statement.setString(2, favourite.getAlbum());
			statement.setString(3, favourite.getArtist());
			statement.setInt(4, favourite.getUserId());

			statement.executeUpdate();

			generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				// get auto increment key
				favourite.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException(
						"Creating room failed, no generated key obtained.");
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

	public List<Favourite> getAllFavourites() {
		// TODO
		throw new NotImplementedException();
	}

}
