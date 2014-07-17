package com.ece452.domain;

import com.ece452.util.MapperIgnore;

public class Favourite extends BaseEntity {
	private int id;
	private String userId;
	private int songId;
	@MapperIgnore
	private Song song;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getSongId() {
		return songId;
	}

	public void setSongId(int songId) {
		this.songId = songId;
	}

	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}

}
