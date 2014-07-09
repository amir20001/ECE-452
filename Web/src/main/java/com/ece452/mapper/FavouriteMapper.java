package com.ece452.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ece452.domain.Favourite;

public class FavouriteMapper implements RowMapper<Favourite>{
	@Override
	public Favourite mapRow(ResultSet resultSet, int line) throws SQLException {
		Favourite favourite = new Favourite();
		try {
			favourite.map(resultSet);
		} catch (Exception e) {

		}
		return favourite;
	}
}
