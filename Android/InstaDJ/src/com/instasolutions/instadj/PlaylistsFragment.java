package com.instasolutions.instadj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.instasolutions.instadj.util.ServiceGetHelper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class PlaylistsFragment extends Fragment implements OnClickListener, OnItemClickListener, OnItemLongClickListener{
	
	Activity activity;
	SparseArray<PlaylistData> playlists = new SparseArray<PlaylistData>();
	ListView lv = null;
	int activeItem = 0;

	/** Called when the activity is first created. */
	@Override
	 public View onCreateView(LayoutInflater inflater, 
	            ViewGroup container, 
	            Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.fragment_playlists, container, false);
	        activity = this.getActivity();
	        return view;
	    }
	
    @Override
	public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
    	
    	Button NewPlaylistButton = (Button)activity.findViewById(R.id.playlists_new_button);
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
    	NewPlaylistButton.setOnClickListener(this);
        lv = (ListView)activity.findViewById(R.id.playlists_list);
        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);
    	
    	ServiceGetHelper getHelper = new ServiceGetHelper(){
    		@Override
    		 protected void onPostExecute(String result) {
    			try {
					JSONArray jPlaylistsArray = new JSONArray(result);
					for(int i = 0; i <jPlaylistsArray.length(); i++){
						JSONObject jPlaylist = jPlaylistsArray.getJSONObject(i);

						playlists.append(i, new PlaylistData(jPlaylist.getInt("id"),
								jPlaylist.getString("name"),
								jPlaylist.getString("genre"),
								jPlaylist.getInt("trackCount"),
								new SparseArray<SongData>()));
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
    	        PlayListAdapter adapter = new PlayListAdapter(activity, playlists );
    	        adapter.setButtonsEnabled(true);;
    	        lv.setAdapter(adapter);
    	        
    	    }

    	};

        getHelper.execute("http://instadj.amir20001.cloudbees.net/playlist/getbyuser/" + prefs.getString("UserID", "0"));

		
    }
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.playlists_new_button:
				FragmentManager fragmentManager = this.getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new NewPlaylistFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
				break;
			default:
				break;
		}
		
	}
	
	public void addPlaylist(PlaylistData playlist)
	{
		playlists.append(playlists.size(), playlist);
	}
	
	public ListView getPlaylistsListView()
	{
		return lv;
	}

	@Override
	public void onItemClick(AdapterView<?> av, View v, int i, long l) {
		PlaylistViewFragment fragment = new PlaylistViewFragment();
		fragment.setPlaylist((PlaylistData)av.getAdapter().getItem(i));
		FragmentManager fragmentManager = this.getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
		
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> av, View v, int i, long l) {
		
		
		activeItem = i;
		return true;
	}

}
