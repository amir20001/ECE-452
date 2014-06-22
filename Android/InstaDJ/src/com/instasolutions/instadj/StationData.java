package com.instasolutions.instadj;

import android.app.Activity;

public class StationData {
	
	public String Name;
	public UserData Owner;
	public PlaylistData Playlist;
	public SongData Song;
	public int ListenerCount;
	
	public StationData(){
		this.Name = "";
		this.Owner = null;
		this.Playlist = null;
		this.Song = null;
		this.ListenerCount = 0;
	}
	
	public StationData(Activity activity, String name, UserData owner, PlaylistData playlist, SongData song, int listenerCount){
		this.Name = name;
		this.Owner = owner;
		this.Playlist = playlist;
		this.Song = song;
		this.ListenerCount = listenerCount;
	}
}
