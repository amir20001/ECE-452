package com.instasolutions.instadj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.instasolutions.instadj.util.ServiceGetHelper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

public class FavoritesFragment extends Fragment {

	/** Called when the activity is first created. */
	SparseArray<SongData> songs = new SparseArray<SongData>();
    Activity activity = null;
    ListView favoritesListView = null;
    
	@Override
	 public View onCreateView(LayoutInflater inflater, 
	            ViewGroup container, 
	            Bundle savedInstanceState) {
	               
			View view = inflater.inflate(R.layout.fragment_favorites, container, false);
	        activity = this.getActivity();
            activity.getActionBar().setTitle(R.string.title_section3);
	        return view;
	    }
	
	 @Override
		public void onActivityCreated(Bundle savedInstanceState)
	    {
	    	super.onActivityCreated(savedInstanceState);
	    	songs.clear();
	    	final ProgressBar pbar = (ProgressBar)activity.findViewById(R.id.favorites_progressbar);
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
	        favoritesListView = (ListView)activity.findViewById(R.id.favorites_list);
	        
	        pbar.setVisibility(ImageView.VISIBLE);
	        
	        ServiceGetHelper getHelper = new ServiceGetHelper(){
	    		@Override
	    		 protected void onPostExecute(String result) {
	    			try {
						JSONArray jSongArray = new JSONArray(result);
						for(int i = 0; i < jSongArray.length(); i++){
							JSONObject jSong = jSongArray.getJSONObject(i);

							songs.append(i, new SongData(jSong.getInt("id"),
											jSong.getString("title"),
											jSong.getString("artist"),
											jSong.getString("album"),
											jSong.getString("duration"),
											null,
											jSong.getString("artUrl"),
											"",
											0));
							
						}
		    	        SongListAdapter adapter = new SongListAdapter(activity, songs);
		    	        adapter.setButtonsEnabled(true);;
		    			favoritesListView.setAdapter(adapter);
		    	        pbar.setVisibility(ImageView.INVISIBLE);
		    	        
					} catch (JSONException e) {
						e.printStackTrace();
					}
	    	        
	    	        
	    	    }

	    	};

	        getHelper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
    				"http://instadj.amir20001.cloudbees.net/favourite/get/" + prefs.getString("UserID", "0"));	
	    }

}
