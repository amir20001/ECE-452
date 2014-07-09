package com.instasolutions.instadj;

import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StationListAdapter extends BaseAdapter{

	private Activity activity;
	private SparseArray<StationData> stations = new SparseArray<StationData>();
	private static LayoutInflater inflater = null;
	
	public StationListAdapter(Activity a, SparseArray<StationData> d)
	{
		activity = a;
		stations = d;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		
		return stations.size();
	}

	@Override
	public Object getItem(int pos) {
		return stations.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View view, ViewGroup viewgroup) {
		View v = view;
		if(view==null)
			v = inflater.inflate(R.layout.list_row_stations, null);
		
		TextView StationName = (TextView)v.findViewById(R.id.station_name);
		TextView StationOwner = (TextView)v.findViewById(R.id.station_owner);
		TextView StationPlaylist = (TextView)v.findViewById(R.id.station_playlist_name);
		TextView StationPlaylistGenre = (TextView)v.findViewById(R.id.station_playlist_genre);
		TextView StationSongName = (TextView)v.findViewById(R.id.station_song_name);
		TextView StationSongArtist = (TextView)v.findViewById(R.id.station_song_artist);
		TextView StationListeningCount = (TextView)v.findViewById(R.id.station_listener_count);
		ImageView StationSongArt = (ImageView)v.findViewById(R.id.station_song_art);
			
		StationData station = stations.get(pos);
		
		StationName.setText(station.Name);
		StationOwner.setText(station.Owner.FirstName + " " + station.Owner.LastName);
		StationPlaylist.setText(station.Playlist.Name);
		StationPlaylistGenre.setText(station.Playlist.Genre);
		StationSongName.setText(station.Song.Title);
		StationSongArtist.setText(station.Song.Artist);
		StationListeningCount.setText(String.valueOf(station.ListenerCount));

		final GettArtworkTask task = new GettArtworkTask(StationSongArt);
		task.execute(station.Song.Art_URL);
		
		return v;
	}

}
