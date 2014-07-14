package com.ece452.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ece452.domain.Follow;
import com.ece452.domain.User;

public class FollowMapper implements RowMapper<Follow> {

	@Override
	public Follow mapRow(ResultSet resultSet, int line) throws SQLException {
		Follow follow = new Follow();
		try {
			follow.map(resultSet);
		} catch (Exception e) {

		}
		return follow;
	}

}
