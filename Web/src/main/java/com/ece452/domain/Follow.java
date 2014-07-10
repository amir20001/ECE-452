package com.ece452.domain;

public class Follow extends BaseEntity {
	private int id;
	private String followingId;
	private String followedId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFollowing() {
		return followingId;
	}

	public void setFollowing(String following) {
		this.followingId = following;
	}

	public String getFollowed() {
		return followedId;
	}

	public void setFollowed(String followed) {
		this.followedId = followed;
	}
}
