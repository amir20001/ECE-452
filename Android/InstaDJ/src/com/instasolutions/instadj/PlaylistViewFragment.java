package com.instasolutions.instadj;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class PlaylistViewFragment extends ListFragment {
	
	private PlaylistData mPlaylist = new PlaylistData();
	
	/** Called when the activity is first created. */
	@Override
	 public View onCreateView(LayoutInflater inflater, 
	            ViewGroup container, 
	            Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
	        SongListAdapter adapter = new SongListAdapter(this.getActivity(), mPlaylist.Songs);
	        setListAdapter(adapter);
	        return view;
	    }
	
	public void setPlaylist(PlaylistData playlist)
	{ 
		mPlaylist = playlist;
	}

}
