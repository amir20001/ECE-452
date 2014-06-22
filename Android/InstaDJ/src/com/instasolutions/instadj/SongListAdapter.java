package com.instasolutions.instadj;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SongListAdapter extends BaseAdapter{

	private Activity activity;
	private SparseArray<SongData> songs = new SparseArray<SongData>();
	private static LayoutInflater inflater = null;
	
	public SongListAdapter(Activity a, SparseArray<SongData> d)
	{
		activity = a;
		songs = d;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		
		return songs.size();
	}

	@Override
	public Object getItem(int pos) {
		return songs.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View view, ViewGroup viewgroup) {
		View v = view;
		if(view==null)
			v = inflater.inflate(R.layout.list_row_songs, null);
		
		TextView title = (TextView)v.findViewById(R.id.song_title);
		TextView artist = (TextView)v.findViewById(R.id.song_artist);
		TextView album = (TextView)v.findViewById(R.id.song_album);
		TextView duration = (TextView)v.findViewById(R.id.song_duration);
		ImageView art = (ImageView)v.findViewById(R.id.list_album_art);
			
		SongData song = songs.get(pos);
		
		title.setText(song.Title);
		artist.setText(song.Artist);
		album.setText(song.Album);
		duration.setText(song.Duration);
		
	    art.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.blankart));
		
		final DownloadArtworkTask task = new DownloadArtworkTask(art);
		task.execute(song.Art_URL);
		
		return v;
	}

}
