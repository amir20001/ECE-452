package com.ece452.domain;

public class User extends BaseEntity {
	private String userID;
	private String firstName;
	private String lastName;
	private int score;
	private int roomId;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstname) {
		this.firstName = firstname;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastname) {
		this.lastName = lastname;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getUserId() {
		return userID;
	}

	public void setUserId(String userid) {
		this.userID = userid;
	}

}
