package com.instasolutions.instadj;

import android.app.Activity;

public class StationData {
	
	public int id;
	public String Name;
	public UserData Owner;
	public PlaylistData Playlist;
	public SongData Song;
	public int ListenerCount;
	
	public StationData(){
		
		this.id = -1;
		this.Name = "";
		this.Owner = null;
		this.Playlist = null;
		this.Song = null;
		this.ListenerCount = 0;
	}
	
	public StationData(int id, String name, UserData owner, PlaylistData playlist, SongData song, int listenerCount){
		this.id = id;
		this.Name = name;
		this.Owner = owner;
		this.Playlist = playlist;
		this.Song = song;
		this.ListenerCount = listenerCount;
	}
}
