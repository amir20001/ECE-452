package com.instasolutions.instadj;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.instasolutions.instadj.util.ServiceGetHelper;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.ListView;

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
	
	//Only populate songs when a playlits is selected for use to prevent excess server calls 
	public void populateSongs()
	{
    	ServiceGetHelper getHelper = new ServiceGetHelper();
    	String result = "";
    	getHelper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
				"http://instadj.amir20001.cloudbees.net/song/getforplaylist/" + String.valueOf(this.id));
    	try {
			result = getHelper.get(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	try {
			JSONArray jSongArray = new JSONArray(result);
			for(int i = 0; i <jSongArray.length(); i++){
				JSONObject jSong = jSongArray.getJSONObject(i);
				SongData song = new SongData(jSong.getInt("id"),
									jSong.getString("title"),
									jSong.getString("artist"),
									jSong.getString("album"),
									jSong.getString("duration"),
									jSong.getString("songUri"),
									jSong.getString("artUrl"),
									jSong.getString("songUrl"),
									jSong.getInt("netScore"));

				this.Songs.append(i, song);
				
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
