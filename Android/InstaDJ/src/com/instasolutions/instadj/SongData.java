package com.instasolutions.instadj;

import android.app.Activity;
import android.graphics.Bitmap;

public class SongData {
	
	public int id;
	public String Title;
	public String Artist;
	public String Album;
	public String Duration;
	public String LocalPath;
	public String Art_URL;
	public Bitmap Art_Bitmap;
	public String Song_URL;
	
	public SongData()
	{
		this.id = -1;
		this.Title = "";
		this.Artist = "";
		this.Duration = "0:00";
		this.Album = "";
		this.LocalPath = "";
		this.Art_URL = "";
		this.Song_URL = "";
	}
	public SongData(int id, String Title, String Artist, String Album, String Duration, String LocalPath, String Art_URL, String Song_URL){
		this.id = id;
		this.Title = Title;
		this.Artist = Artist;
		this.Album = Album;
		this.Duration = Duration;
		this.LocalPath = LocalPath;
		this.Art_URL = Art_URL;
		this.Song_URL = Song_URL;
	}
	
	public SongData(int id, String Title, String Artist, String Album, String Duration, String LocalPath, Bitmap Art_Bitmap, String Song_URL){
		this.id = id;
		this.Title = Title;
		this.Artist = Artist;
		this.Album = Album;
		this.Duration = Duration;
		this.LocalPath = LocalPath;
		this.Art_Bitmap = Art_Bitmap;
		this.Song_URL = Song_URL;
		
	}
	
	public SongData(int id, String Title, String Artist, String Album, String Duration, String LocalPath, String Song_URL){
		this.id = id;
		this.Title = Title;
		this.Artist = Artist;
		this.Album = Album;
		this.Duration = Duration;
		this.LocalPath = LocalPath;		
		this.Song_URL = Song_URL;
	}
	
}
