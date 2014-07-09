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

import com.ece452.domain.User;
import com.ece452.mapper.UserMapper;

@Repository
public class UserDao {

	private DataSource dataSource;

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.dataSource = dataSource;
	}

	public User inset(User user) {

		String sql = "INSERT INTO user (username,picture_url,score,room_id) VALUES (?,?,?,?)";
		ResultSet generatedKeys = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getPictureUrl());
			statement.setInt(3, user.getScore());
			statement.setInt(4, user.getRoomId());

			generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				// get auto increment key
				user.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException(
						"Creating user failed, no generated key obtained.");
			}
			generatedKeys.close();
			statement.close();
			return user;
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

	public User getUserbyId(int id) {
		User user = null;
		String sql = "SELECT * FROM user WHERE id= ?";

		try {
			user = jdbcTemplate.queryForObject(sql, new Object[] { id },
					new UserMapper());
		} catch (Exception e) {
			// No user was found with the specified id, return null
			return null;
		}
		return user;
	}

	public User getUser(String username) {
		User user = null;
		String sql = "SELECT * FROM user WHERE username = ?";

		try {
			user = jdbcTemplate.queryForObject(sql, new Object[] { username },
					new UserMapper());
		} catch (Exception e) {
			// No user was found with the specified id, return null
			return null;
		}
		return user;
	}

	public List<User> getAllUsers() {
		String sql = "SELECT * FROM user;";
		List<User> user = new ArrayList<User>();
		try {
			user = jdbcTemplate.query(sql, new UserMapper());
			return user;
		} catch (Exception e) {
			return user;
		}
	}

}
