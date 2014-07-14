package com.ece452.domain;

import com.ece452.util.Content;

public class Sync {

	public static final transient String sync = "sync";
	public static final transient String kick = "kick";
	public static final transient String score = "score";


	private String action;
	private int roomId;
	private int songId;
	private int position;
	private int songScore;

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

	public Content addToContent(Content content) {

		content.createData("action", action);
		content.createData("songId", new Integer(songId).toString());
		content.createData("position", new Integer(position).toString());
		content.createData("songScore", new Integer(songScore).toString());

		return content;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

}
