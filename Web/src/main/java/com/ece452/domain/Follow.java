package com.ece452.domain;

public class Follow extends BaseEntity {
	private int id;
	private String followerId;
	private String followeeId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFollowerId() {
		return followerId;
	}

	public void setFollowerId(String followerId) {
		this.followerId = followerId;
	}

	public String getFolloweeId() {
		return followeeId;
	}

	public void setFolloweeId(String followeeId) {
		this.followeeId = followeeId;
	}

}
