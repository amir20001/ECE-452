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
import com.ece452.mapper.GcmIdMapper;
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

	public User insert(User user) {

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

	public String updateGCMId(String id, String userid) {
		String sql = "UPDATE user SET gcm_id=? WHERE user_id=?;";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, id);
			statement.setString(2, userid);
			statement.executeUpdate();
			statement.close();
			return userid;
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

	public void updateRoom(String userid, int roomId) {
		String sql = "UPDATE user SET room_id=? WHERE user_id=?;";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, roomId);
			statement.setString(2, userid);
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

	public void updateScore(String userId, int delta) {
		String sql = "UPDATE user SET score=score+ ? WHERE user_id =?";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement;
			statement = conn.prepareStatement(sql);
			statement.setInt(1, delta);
			statement.setString(2, userId);
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

	public void updateAllRoomRefs(int roomId) {
		String sql = "	UPDATE USER SET room_id =0 WHERE room_id =?";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement statement;
			statement = conn.prepareStatement(sql);
			statement.setInt(1, roomId);
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

	public List<User> getUsersInRoom(int roomId) {
		String sql = "SELECT * FROM USER WHERE room_id =?";

		List<User> user = new ArrayList<User>();
		try {
			user = jdbcTemplate.query(sql, new Object[] { roomId },
					new UserMapper());
			return user;
		} catch (Exception e) {
			return user;
		}
	}

	public List<String> getGcmInRoom(int roomId) {
		String sql = "SELECT gcm_id FROM USER WHERE room_id = ?";

		List<String> gcmIds = new ArrayList<String>();
		try {
			gcmIds = jdbcTemplate.query(sql, new Object[] { roomId },
					new GcmIdMapper());
			return gcmIds;
		} catch (Exception e) {
			return gcmIds;
		}
	}

	public List<String> getGcmOfAllUsersInRoom() {
		String sql = "SELECT gcm_id FROM USER ";

		List<String> gcmIds = new ArrayList<String>();
		try {
			gcmIds = jdbcTemplate.query(sql, new GcmIdMapper());
			return gcmIds;
		} catch (Exception e) {
			return gcmIds;
		}
	}

}
