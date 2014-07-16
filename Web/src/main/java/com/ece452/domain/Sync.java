package com.ece452.domain;

public class Sync {

	public static final transient String sync = "sync";
	public static final transient String kick = "kick";
	public static final transient String score = "score";
	public static final transient String heartbeat = "heartbeat";
	public static final transient String roompause= "roompause";
	public static final transient String roomplay= "roomplay";
	
	private String action;
	private Song song;
	private Room room;

	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
