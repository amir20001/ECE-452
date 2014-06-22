package com.instasolutions.instadj;



import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlayListAdapter extends BaseAdapter{

	private Activity activity;
	private SparseArray<PlaylistData> playlists = new SparseArray<PlaylistData>();
	private static LayoutInflater inflater = null;
	
	public PlayListAdapter(Activity a, SparseArray<PlaylistData> d)
	{
		activity = a;
		playlists = d;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		
		return playlists.size();
	}

	@Override
	public Object getItem(int pos) {
		return playlists.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View view, ViewGroup viewgroup) {
		View v = view;
		if(view==null)
			v = inflater.inflate(R.layout.list_row_playlists, null);
		
		TextView PlaylistName = (TextView)v.findViewById(R.id.playlist_name);
		TextView PlaylistGenre = (TextView)v.findViewById(R.id.playlist_genre);
		TextView TrackCount = (TextView)v.findViewById(R.id.playlsit_tracks);
			
		PlaylistData playlist = playlists.get(pos);
		
		PlaylistName.setText(playlist.Name);
		PlaylistGenre.setText(playlist.Genre);
		TrackCount.setText(String.valueOf(playlist.TrackCount));
				
		
		return v;
	}

}
