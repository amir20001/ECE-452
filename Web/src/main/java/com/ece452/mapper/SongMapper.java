package com.ece452.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ece452.domain.Song;

public class SongMapper implements RowMapper<Song> {
	@Override
	public Song mapRow(ResultSet resultSet, int line) throws SQLException {
		Song song = new Song();
		try {
			song.map(resultSet);
		} catch (Exception e) {

		}
		return song;
	}

}
