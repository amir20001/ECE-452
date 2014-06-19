package com.ece452.domain;

public class Room {
	private int id;
	private String name;
	private String ownerUserName;
	private int listenerCount;
	private int currentSongID;

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

	public String getOwnerUserName() {
		return ownerUserName;
	}

	public void setOwnerUserName(String ownerUserName) {
		this.ownerUserName = ownerUserName;
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

}
