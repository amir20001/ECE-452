package com.instasolutions.instadj;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class FavoritesFragment extends ListFragment {

	/** Called when the activity is first created. */
	@Override
	 public View onCreateView(LayoutInflater inflater, 
	            ViewGroup container, 
	            Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
	        SparseArray<SongData> songs = new SparseArray<SongData>();
	        songs.append(0, new SongData(this.getActivity(), "TestTitle", "TestArtist", "TestAlbum", "1:00", "http://imgs.tuts.dragoart.com/how-to-draw-the-green-day-heart-grenade-letters_1_000000000968_5.jpg"));
	        songs.append(1, new SongData(this.getActivity(), "TestTitle2", "TestArtist2", "TestAlbum2", "2:00", "http://artsorigin.com/blog/wp-content/uploads/2009/05/graduation-album-cover.jpg"));
	        SongListAdapter adapter = new SongListAdapter(this.getActivity(), songs );
	        setListAdapter(adapter);
	        return view;
	    }

}
