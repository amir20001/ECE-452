package com.instasolutions.instadj;

import android.app.Activity;
import android.graphics.Bitmap;

public class SongData {
	
	public String Title;
	public String Artist;
	public String Album;
	public String Duration;
	public String LocalPath;
	public String Art_URL;
	public Bitmap Art_Bitmap;
	
	public SongData()
	{
		this.Title = "";
		this.Artist = "";
		this.Duration = "0:00";
		this.Album = "";
		this.LocalPath = "";
		this.Art_URL = "";
	}
	public SongData(String Title, String Artist, String Album, String Duration, String LocalPath, String Art_URL){
		this.Title = Title;
		this.Artist = Artist;
		this.Album = Album;
		this.Duration = Duration;
		this.LocalPath = LocalPath;
		this.Art_URL = Art_URL;
		
	}
	
	public SongData(String Title, String Artist, String Album, String Duration, String LocalPath, Bitmap Art_Bitmap){
		this.Title = Title;
		this.Artist = Artist;
		this.Album = Album;
		this.Duration = Duration;
		this.LocalPath = LocalPath;
		this.Art_Bitmap = Art_Bitmap;
		
	}
	
	public SongData(String Title, String Artist, String Album, String Duration, String LocalPath){
		this.Title = Title;
		this.Artist = Artist;
		this.Album = Album;
		this.Duration = Duration;
		this.LocalPath = LocalPath;		
	}
	
}
