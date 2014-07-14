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
import com.ece452.mapper.FollowMapper;
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
		String sql = "INSERT INTO follow (follower,followee) VALUES (?,?)";
		ResultSet generatedKeys = null;
		Connection conn = null;

		try {
			conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, follow.getFollowerId());
			statement.setString(2, follow.getFolloweeId());

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

	public List<User> getAllFollowers(String userId) {
		String sql = "SELECT user.* FROM follow INNER JOIN `user` ON user.user_id = follow.follower AND follow.followee = ?;";
		List<User> users = new ArrayList<User>();
		try {
			users = jdbcTemplate.query(sql, new Object[] { userId }, new UserMapper());
			return users;
		} catch (Exception e) {
			return users;
		}
	}

	public List<User> getAllFollowees(String userId) {
		String sql = "SELECT user.* FROM follow INNER JOIN `user` ON user.user_id = follow.followee AND follow.follower = ?;";
		List<User> users = new ArrayList<User>();
		try {
			users = jdbcTemplate.query(sql, new Object[] { userId }, new UserMapper());
			return users;
		} catch (Exception e) {
			return users;
		}
	}
	
	public List<Follow> getId(String followerId, String followeeId)
	{
		String sql = "SELECT * FROM follow WHERE follower = ? AND followee = ?;";
		List<Follow> followList = new ArrayList<Follow>();
		try {
			followList = jdbcTemplate.query(sql, new Object[] {followerId, followeeId}, new FollowMapper());
			return followList;
		} catch (Exception e) {
			return followList;
		}
	}

}
