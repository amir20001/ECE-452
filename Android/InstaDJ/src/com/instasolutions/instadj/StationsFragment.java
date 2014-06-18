package com.instasolutions.instadj;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class StationsFragment extends ListFragment {
	
	Activity activity = null;
	/** Called when the activity is first created. */
	@Override
	 public View onCreateView(LayoutInflater inflater, 
	            ViewGroup container, 
	            Bundle savedInstanceState) {
			activity = this.getActivity();
	        View view = inflater.inflate(R.layout.fragment_stations, container, false);
	        SparseArray<StationData> stations = new SparseArray<StationData>();
	        stations.append(0, new StationData(activity, "Station 1", 
	        		new UserData(this.getActivity(), "TestUser", ""), 
	        		new PlaylistData(activity, "Playlist1", "Pop", 10), 
	        		new SongData(activity, "SongName1", "Artist1", "Album1", "2:00", "", "http://artsorigin.com/blog/wp-content/uploads/2009/05/graduation-album-cover.jpg"), 
	        		2));
	        stations.append(1, new StationData(activity, "Station 2", 
	        		new UserData(this.getActivity(), "TestUser2", ""), 
	        		new PlaylistData(activity, "Playlist1", "Pop", 10), 
	        		new SongData(activity, "SongName2", "Artist2", "Album2", "1:00", "", ""), 
	        		5));
	        StationListAdapter adapter = new StationListAdapter(this.getActivity(), stations );
	        setListAdapter(adapter);
	        return view;
	    }

}
