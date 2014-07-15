package com.ece452.domain;


public class Sync {

	public static final transient String sync = "sync";
	public static final transient String kick = "kick";
	public static final transient String score = "score";


	private String action;
	private int roomId;
	private int songId;
	private int position;
	private int songScore;
	private Song song;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getSongId() {
		return songId;
	}

	public void setSongId(int songId) {
		this.songId = songId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getSongScore() {
		return songScore;
	}

	public void setSongScore(int songScore) {
		this.songScore = songScore;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}

}
