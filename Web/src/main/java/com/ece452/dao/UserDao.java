package com.ece452.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

		String sql = "INSERT INTO room (username,picture_url) VALUES (?,?)";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getPictureUrl());

			statement.executeUpdate();
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

	public User getUser (String username) {
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

}
