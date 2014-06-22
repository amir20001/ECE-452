package com.instasolutions.instadj;

import android.app.Activity;
import android.util.SparseArray;

public class PlaylistData {
	
	public String Name;
	public String Genre;
	public int TrackCount;
	public SparseArray<SongData> Songs = new SparseArray<SongData>();
	
	public PlaylistData()
	{
		this.Name = "";
		this.Genre = "";
		this.TrackCount = 0;
	}
	public PlaylistData(Activity activity, String name, String genre, int trackCount){
		this.Name = name;
		this.Genre = genre;
		this.TrackCount = trackCount;
	}
	
	public PlaylistData(Activity activity, String name, String genre, int trackCount, SparseArray<SongData> songs){
		this.Name = name;
		this.Genre = genre;
		this.TrackCount = trackCount;
		this.Songs = songs;
	}
}
