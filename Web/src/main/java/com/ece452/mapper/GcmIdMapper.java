package com.ece452.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class GcmIdMapper implements RowMapper<String> {

	@Override
	public String mapRow(ResultSet resultSet, int line) throws SQLException {
		String gcmId = null;
		try {
			gcmId = resultSet.getString("gcm_id");
		} catch (Exception e) {

		}
		return gcmId;
	}
}
