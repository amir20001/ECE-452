package com.ece452.domain;

import com.ece452.util.MapperIgnore;

public class Room extends BaseEntity {
	private int id;
	private String name;
	private String ownerUserId;
	private int listenerCount;
	private int currentSongId;
	private int playlistId;
	private int songPosition;
	private boolean songIsPlaying;
	private long songPlayStartTime;

	@MapperIgnore
	private User user;
	@MapperIgnore
	private Playlist playlist;
	@MapperIgnore
	private Song song;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Id: " + id + ",Name: " + name;
	}

	public int getListenerCount() {
		return listenerCount;
	}

	public void setListenerCount(int listenerCount) {
		this.listenerCount = listenerCount;
	}

	public int getPlaylistId() {
		return playlistId;
	}

	public void setPlaylistId(int playlistId) {
		this.playlistId = playlistId;
	}

	public String getOwnerUserId() {
		return ownerUserId;
	}

	public void setOwnerUserId(String ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Playlist getPlaylist() {
		return playlist;
	}

	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}

	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}

	public int getCurrentSongId() {
		return currentSongId;
	}

	public void setCurrentSongId(int currentSongId) {
		this.currentSongId = currentSongId;
	}

	public int getSongPosition() {
		return songPosition;
	}

	public void setSongPosition(int songPosition) {
		this.songPosition = songPosition;
	}

	public boolean isSongIsPlaying() {
		return songIsPlaying;
	}

	public void setSongIsPlaying(boolean songIsPlaying) {
		this.songIsPlaying = songIsPlaying;
	}

	public long getSongPlayStartTime() {
		return songPlayStartTime;
	}

	public void setSongPlayStartTime(long songPlayStartTime) {
		this.songPlayStartTime = songPlayStartTime;
	}

}
