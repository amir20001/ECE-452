package com.ece452.domain;

public class Favourite {
	private String songName;
	private String artist;
					
	@Override
	public String toString() {
		return "Song Name: " + getSongName() + ", Artist: " + getArtist();
	}

	public String getSongName() {
		return songName;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}
}
