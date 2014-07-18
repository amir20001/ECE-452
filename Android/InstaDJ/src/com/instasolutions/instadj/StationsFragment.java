package com.instasolutions.instadj;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.instasolutions.instadj.util.ServiceGetHelper;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class StationsFragment extends Fragment implements OnClickListener, OnItemClickListener {
	
	Activity activity = null;
	ListView stationListView = null;
	SparseArray<StationData> stations = new SparseArray<StationData>();
    StationListAdapter stationlist_adapter = null;
    int selected_filter = 0;

	/** Called when the activity is first created. */
	@Override
	 public View onCreateView(LayoutInflater inflater, 
	            ViewGroup container, 
	            Bundle savedInstanceState) {
			activity = this.getActivity();
	        View view = inflater.inflate(R.layout.fragment_stations, container, false);
            this.setHasOptionsMenu(true);
            setDefaultUncaughtExceptionHandler();
            activity.getActionBar().setTitle(R.string.title_section2);

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
    	        stationlist_adapter = new StationListAdapter(activity, stations );
    	        stationListView.setAdapter(stationlist_adapter);
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


    private static void setDefaultUncaughtExceptionHandler() {
        try {
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    Log.e("InstaDJ", "Uncaught Exception detected in thread {}" + t.getName() + e.getMessage());
                }
            });
        } catch (SecurityException e) {
            Log.e("InstaDJ", "Uncaught Exception detected in thread {}" + e.getMessage());
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater){
        // Inflate the menu items for use in the action bar

        inflater.inflate(R.menu.search, menu);



//        View v = menu.findItem(R.id.magnifying_glass).getActionView();
//        EditText searchField = (EditText) v.findViewById(R.id.searchField);
//        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                Log.i("INSTADJ", "Search was clicked");
//                return true;
//            }
//        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // Handle item selection
        if (menuItem.getItemId() == R.id.magnifying_glass) {
            menuItem.setActionView(R.layout.search_layout).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            Spinner sp = (Spinner) menuItem.getActionView().findViewById(R.id.filter_types);
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selected_filter = i;
                    Log.i("InstaDJ", String.valueOf(i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // DO NOTHING
                }
            });
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                    R.array.station_filters, R.layout.custom_spinner);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.custom_spinner);
            // Apply the adapter to the spinner
            sp.setAdapter(adapter);
            EditText et = (EditText) menuItem.getActionView().findViewById(R.id.searchField);
            et.requestFocus();
//            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);

            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    try {
                       stationlist_adapter.getFilter(selected_filter).filter(charSequence);
                    } catch (Exception e) {
                        Log.e("InstaDJ", e.getMessage());
                        Log.e("InstaDJ", e.getStackTrace().toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
        return true;
    }
}
