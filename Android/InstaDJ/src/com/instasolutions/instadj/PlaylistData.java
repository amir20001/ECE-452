package com.instasolutions.instadj;

import android.app.Activity;
import android.util.SparseArray;

public class PlaylistData {
	
	public int id;
	public String Name;
	public String Genre;
	public int TrackCount;
	public SparseArray<SongData> Songs = new SparseArray<SongData>();
	
	public PlaylistData()
	{
		this.id = -1;
		this.Name = "";
		this.Genre = "";
		this.TrackCount = 0;
	}
	public PlaylistData(int id, String name, String genre, int trackCount){
		this.id = id;
		this.Name = name;
		this.Genre = genre;
		this.TrackCount = trackCount;
	}
	
	public PlaylistData(int id, String name, String genre, int trackCount, SparseArray<SongData> songs){
		this.id = id;
		this.Name = name;
		this.Genre = genre;
		this.TrackCount = trackCount;
		this.Songs = songs;
	}
}
