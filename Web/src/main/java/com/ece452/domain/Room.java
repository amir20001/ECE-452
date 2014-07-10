package com.ece452.domain;

public class Room extends BaseEntity {
	private int id;
	private String name;
	private String ownerUserId;
	private int listenerCount;
	private int currentSongID;
	private int playlistId;
	@MapperIgnore
	private User user;
	@MapperIgnore
	private Playlist playlist;

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

	public int getCurrentSongID() {
		return currentSongID;
	}

	public void setCurrentSongID(int currentSongID) {
		this.currentSongID = currentSongID;
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

}
