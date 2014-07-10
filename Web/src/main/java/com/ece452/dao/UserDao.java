package com.ece452.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

		String sql = "INSERT INTO user (user_id,first_name, last_name, score,room_id) VALUES (?,?,?,?,?);";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, user.getUserId());
			statement.setString(2, user.getFirstName());
			statement.setString(3, user.getLastName());
			statement.setInt(4, user.getScore());
			statement.setInt(5, user.getRoomId());
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


	public User getUser(String userid) {
		User user = null;
		String sql = "SELECT * FROM user WHERE user_id = ?";

		try {
			user = jdbcTemplate.queryForObject(sql, new Object[] { userid },
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
