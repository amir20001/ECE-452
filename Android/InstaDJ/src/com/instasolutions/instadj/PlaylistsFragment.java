package com.instasolutions.instadj;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class PlaylistsFragment extends ListFragment {

	/** Called when the activity is first created. */
	@Override
	 public View onCreateView(LayoutInflater inflater, 
	            ViewGroup container, 
	            Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.fragment_playlists, container, false);
	        SparseArray<PlaylistData> playlists = new SparseArray<PlaylistData>();
	        playlists.append(0, new PlaylistData(this.getActivity(), "Playlist1", "Pop", 10));
	        playlists.append(1, new PlaylistData(this.getActivity(), "Playlist2", "Mix", 50));
	        PlayListAdapter adapter = new PlayListAdapter(this.getActivity(), playlists );
	        setListAdapter(adapter);
	        return view;
	    }

}
