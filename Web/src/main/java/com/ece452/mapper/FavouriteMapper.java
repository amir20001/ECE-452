package com.ece452.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ece452.domain.Favourite;
import com.ece452.domain.Song;

public class FavouriteMapper implements RowMapper<Favourite> {
	@Override
	public Favourite mapRow(ResultSet resultSet, int line) throws SQLException {
		Favourite favourite = new Favourite();
		try {
			Song song = new Song();
			favourite.map("favourite_", resultSet);
			song.map(resultSet);
			favourite.setSong(song);
		} catch (Exception e) {

		}
		return favourite;
	}
}
