package com.instasolutions.instadj;

import android.app.Activity;

public class SongData {
	
	public String Title;
	public String Artist;
	public String Album;
	public String Duration;
	public String Art_URL;
	
	public SongData(Activity activity, String Title, String Artist, String Album, String Duration, String Art_URL){
		this.Title = Title;
		this.Artist = Artist;
		this.Album = Album;
		this.Duration = Duration;
		this.Art_URL = Art_URL;
		
	}
}
