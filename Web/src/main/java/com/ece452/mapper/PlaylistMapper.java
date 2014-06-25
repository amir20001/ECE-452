package com.ece452.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ece452.domain.Playlist;

public class PlaylistMapper implements RowMapper<Playlist> {

	@Override
	public Playlist mapRow(ResultSet resultSet, int line) throws SQLException {
		Playlist playlist = new Playlist();
		try {
			playlist.map(resultSet);
		} catch (Exception e) {

		}
		return playlist;
	}
}
