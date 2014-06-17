package com.instasolutions.instadj;

import android.app.Activity;

public class PlaylistData {
	
	public String Name;
	public String Genre;
	public int TrackCount;
	
	public PlaylistData(Activity activity, String name, String genre, int trackCount){
		this.Name = name;
		this.Genre = genre;
		this.TrackCount = trackCount;
	}
}
