package com.instasolutions.instadj;

import android.app.Activity;

public class StationData {
	
	public String Name;
	public UserData Owner;
	public PlaylistData Playlist;
	public SongData Song;
	public int ListenerCount;
	
	public StationData(Activity activity, String name, UserData owner, PlaylistData playlist, SongData song, int listenerCount){
		this.Name = name;
		this.Owner = owner;
		this.Playlist = playlist;
		this.Song = song;
		this.ListenerCount = listenerCount;
	}
}
