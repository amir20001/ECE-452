package com.instasolutions.instadj;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class StationsFragment extends Fragment implements OnClickListener, OnItemClickListener {
	
	Activity activity = null;
	SparseArray<StationData> stations = new SparseArray<StationData>();
	/** Called when the activity is first created. */
	@Override
	 public View onCreateView(LayoutInflater inflater, 
	            ViewGroup container, 
	            Bundle savedInstanceState) {
			activity = this.getActivity();
	        View view = inflater.inflate(R.layout.fragment_stations, container, false);
	       
	        return view;
	    }
	
    @Override
	public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
    	
    	Button NewStationButton = (Button)this.getActivity().findViewById(R.id.stations_new_button);
    	NewStationButton.setOnClickListener(this);
    	
	        stations.append(0, new StationData("Station 1", 
	        		new UserData("FirstName1", "LastName1", "USERID1"), 
	        		new PlaylistData(1, "Playlist1", "Pop", 10), 
	        		new SongData("SongName1", "Artist1", "Album1", "2:00", "", "http://artsorigin.com/blog/wp-content/uploads/2009/05/graduation-album-cover.jpg"), 
	        		2));
	        stations.append(1, new StationData("Station 2", 
	        		new UserData("FirstName2", "LastName2", "USERID2"), 
	        		new PlaylistData(2, "Playlist1", "Pop", 10), 
	        		new SongData("SongName2", "Artist2", "Album2", "1:00", "", ""), 
	        		5));
        StationListAdapter adapter = new StationListAdapter(this.getActivity(), stations );
        ListView lv = (ListView)this.getActivity().findViewById(R.id.stations_list);
        lv.setOnItemClickListener(this);
        lv.setAdapter(adapter);
		
    }
	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.stations_new_button:
				FragmentManager fragmentManager = this.getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new NewStationFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
				break;
			default:
				break;
		}
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> av, View v, int i, long l) {
		
	   CurrentRoomFragment fragment = ((ListeningRoom)activity).getCurrentRoomFragment();
	   fragment.setStation((StationData)av.getAdapter().getItem(i));
	   FragmentManager fragmentManager = this.getFragmentManager();
	   FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
       fragmentTransaction.replace(R.id.container, fragment);
       fragmentTransaction.addToBackStack(null);
       fragmentTransaction.commit();
		
	}
	
	
	//TEMPORARY METHODS FOR LOCAL DATA TRANSFER
	//WILL JUST GRAB FROM SERVER WHEN IMPLEMENTED
	public void addStation(StationData station)
	{
		stations.append(stations.size(), station);
	}
	
}
