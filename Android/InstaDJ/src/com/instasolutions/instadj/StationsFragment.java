package com.instasolutions.instadj;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.instasolutions.instadj.util.ServiceGetHelper;

import android.app.Activity;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

public class StationsFragment extends Fragment implements OnClickListener, OnItemClickListener {
	
	Activity activity = null;
	ListView stationListView = null;
	SparseArray<StationData> stations = new SparseArray<StationData>();
	/** Called when the activity is first created. */
	@Override
	 public View onCreateView(LayoutInflater inflater, 
	            ViewGroup container, 
	            Bundle savedInstanceState) {
			activity = this.getActivity();
	        View view = inflater.inflate(R.layout.fragment_stations, container, false);

            activity.getActionBar().setTitle(R.string.StationsTitle);
	       
	        return view;
	    }
	
    @Override
	public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
    	stations.clear();
    	final ProgressBar pbar = (ProgressBar)activity.findViewById(R.id.stations_progressbar);
    	Button NewStationButton = (Button)this.getActivity().findViewById(R.id.stations_new_button);
    	NewStationButton.setOnClickListener(this);
        stationListView = (ListView)this.getActivity().findViewById(R.id.stations_list);
        stationListView.setOnItemClickListener(this);
        
        pbar.setVisibility(ImageView.VISIBLE);
    	
    	ServiceGetHelper getHelper = new ServiceGetHelper(){
    		@Override
    		 protected void onPostExecute(String result) {
    			try {
					JSONArray jStationsArray = new JSONArray(result);
					for(int i = 0; i <jStationsArray.length(); i++){
						JSONObject jStation = jStationsArray.getJSONObject(i);
						JSONObject jPlaylist = jStation.getJSONObject("playlist");
						JSONObject jUser = jStation.getJSONObject("user");
						JSONObject jSong = jStation.getJSONObject("song");
						PlaylistData playlist = new PlaylistData(jStation.getInt("playlistId"),
										jPlaylist.getString("name"),
										jPlaylist.getString("genre"),
										jPlaylist.getInt("trackCount"),
										new SparseArray<SongData>());
						UserData owner = new UserData(jUser.getString("firstName"),
										jUser.getString("lastName"),
										jUser.getString("userId"),
                                        jUser.getString("score"));
						SongData song = new SongData(jStation.getInt("currentSongId"),
													jSong.getString("title"), 
													jSong.getString("artist"),
													jSong.getString("album"),
													jSong.getString("duration"),
													jSong.getString("songUri"),
													jSong.getString("artUrl"),
													jSong.getString("songUrl"),
													jSong.getInt("netScore"));
						
						stations.append(i, new StationData(jStation.getInt("id"),
												jStation.getString("name"),
												owner,
												playlist,
												song,
												jStation.getInt("listenerCount")));
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
    	        StationListAdapter adapter = new StationListAdapter(activity, stations );
    	        stationListView.setAdapter(adapter);
    	        pbar.setVisibility(ImageView.INVISIBLE);
    	        
    	    }

    	};

        getHelper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
				"http://instadj.amir20001.cloudbees.net/room/getall");
		
    }
	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.stations_new_button:
				FragmentManager fragmentManager = this.getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new NewStationFragment(), "NewStationFragment");
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
	   fragment.setStation((StationData)av.getAdapter().getItem(i), activity);
	   FragmentManager fragmentManager = this.getFragmentManager();
	   FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
       fragmentTransaction.replace(R.id.container, fragment, "CurrentRoomFragment");
       fragmentTransaction.addToBackStack(null);
       fragmentTransaction.commit();
		
	}
	
}
