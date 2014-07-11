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

import com.ece452.domain.Follow;
import com.ece452.domain.User;
import com.ece452.mapper.UserMapper;
import com.mysql.jdbc.Statement;

@Repository
public class FollowDao {

	private DataSource dataSource;

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.dataSource = dataSource;
	}

	public Follow insert(Follow follow) {
		String sql = "INSERT INTO follow (follwing,followed) VALUES (?,?)";
		ResultSet generatedKeys = null;
		Connection conn = null;

		try {
			conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, follow.getFollowing());
			statement.setString(2, follow.getFollowed());

			statement.executeUpdate();
			generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				// get auto increment key
				follow.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException("Creating follow failed, no generated key obtained.");
			}
			generatedKeys.close();
			statement.close();
			return follow;
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

	public List<User> getAllFollowing(User user) {
		String sql = "SELECT user.* FROM follow INNER JOIN `user` ON user.user_id = follow.following AND follow.followed = ?;";
		List<User> users = new ArrayList<User>();
		try {
			users = jdbcTemplate.query(sql, new Object[] { user.getUserId() }, new UserMapper());
			return users;
		} catch (Exception e) {
			return users;
		}
	}

	public List<User> getAllFollowed(User user) {
		String sql = "SELECT user.* FROM follow INNER JOIN `user` ON user.user_id = follow.followed AND follow.following = ?;";
		List<User> users = new ArrayList<User>();
		try {
			users = jdbcTemplate.query(sql, new Object[] { user.getUserId() }, new UserMapper());
			return users;
		} catch (Exception e) {
			return users;
		}
	}

}
