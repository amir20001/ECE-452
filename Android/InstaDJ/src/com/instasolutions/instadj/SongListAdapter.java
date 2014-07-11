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
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton;

public class SongListAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener{

	private Activity activity;
	private SparseArray<SongData> songs = new SparseArray<SongData>();
	private SparseBooleanArray checkBoxArray = new SparseBooleanArray();
	private static LayoutInflater inflater = null;
	private Boolean UseSelect = false;
	private Boolean UseButtons = false;
	
	public SongListAdapter(Activity a, SparseArray<SongData> d)
	{
		activity = a;
		songs = d;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	public SongListAdapter(Activity a, SparseArray<SongData> d, Boolean useSelect)
	{
		activity = a;
		songs = d;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		UseSelect = useSelect;
	}
	
	public SparseBooleanArray getCheckBoxArray()
	{
		return checkBoxArray;
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
		if(view==null){
			if(UseSelect)
			{
				v = inflater.inflate(R.layout.list_row_songs_select, null);
			}
			else
				v = inflater.inflate(R.layout.list_row_songs, null);
		}
		
		final TextView title = (TextView)v.findViewById(R.id.song_title);
		TextView artist = (TextView)v.findViewById(R.id.song_artist);
		TextView album = (TextView)v.findViewById(R.id.song_album);
		TextView duration = (TextView)v.findViewById(R.id.song_duration);
		ImageView art = (ImageView)v.findViewById(R.id.list_album_art);
			
		SongData song = songs.get(pos);
		
		title.setText(song.Title);
		artist.setText(song.Artist);
		album.setText(song.Album);
		duration.setText(song.Duration);
		
		if(UseSelect)
		{
			TextView SongPath = (TextView)v.findViewById(R.id.song_contentpath);
			CheckBox checkbox = (CheckBox)v.findViewById(R.id.songs_checkbox);
			Boolean checked = checkBoxArray.get(pos);
			SongPath.setText(song.LocalPath);
			checkbox.setTag(pos);
			checkbox.setOnCheckedChangeListener(null);
			if(checked != null)
			{
				checkbox.setChecked(checked);;
			}
			else
			{
				checkbox.setChecked(false);
				checkBoxArray.append(pos, false);
			}

			checkbox.setOnCheckedChangeListener(this);
			
		}
		else
		{
			ImageButton deleteButton = (ImageButton)v.findViewById(R.id.song_delete_button);
			if(UseButtons)
			{
				deleteButton.setOnClickListener(new OnClickListener() { 
			        @Override 
			        public void onClick(View v) { 
			        	//TODO: Change to remove from server and update list
			            title.setText("Deleted");
			        } 
			    }); 
			}
			else
			{
				deleteButton.setVisibility(ImageView.INVISIBLE);
			}
		}
		
	    art.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.blankart));
		
	    if(song.Art_URL != null && song.Art_URL != "null")
	    {
			final GettArtworkTask task = new GettArtworkTask(art);
			task.execute(song.Art_URL);
	    }
	    else if(song.LocalPath != null && song.LocalPath != "null")
	    {
			final GettArtworkTask task = new GettArtworkTask(art);
			task.execute(song.LocalPath);
	    }
	    else if(song.Art_Bitmap != null)
	    {
	    	art.setImageBitmap(song.Art_Bitmap);
	    }
		
		return v;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(buttonView.isChecked())
			checkBoxArray.put((Integer)buttonView.getTag(), true);
		else
			checkBoxArray.put((Integer)buttonView.getTag(), false);
		
	}
	
	public SparseArray<SongData> getArray()
	{
		return songs;
	}
	
	public void setArray(SparseArray<SongData> sArray)
	{
		songs = sArray;
	}
	
	public void setButtonsEnabled(boolean buttons)
	{
		this.UseButtons = buttons;
	}
	

}
