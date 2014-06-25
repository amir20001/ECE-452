package com.instasolutions.instadj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.content.CursorLoader;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public class NewStationFragment extends Fragment implements OnClickListener, OnItemClickListener{
	
	ListView playlistsListView = null;
	int selectedPlaylistPos = 0;

	/** Called when the activity is first created. */
	@Override
	 public View onCreateView(LayoutInflater inflater, 
	            ViewGroup container, 
	            Bundle savedInstanceState) {

	        View view = inflater.inflate(R.layout.fragment_newstation, container, false);
	        
	        return view;
	    }
	
    @Override
	public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
		
    	Button cancelButton = (Button)this.getActivity().findViewById(R.id.newstation_cancel_button);
    	Button saveButton = (Button)this.getActivity().findViewById(R.id.newstation_save_button);
    	playlistsListView = (ListView)this.getActivity().findViewById(R.id.newstation_playlistlist);
    	
    	cancelButton.setOnClickListener(this);
    	saveButton.setOnClickListener(this);
    	if(((ListeningRoom)getActivity()).getPlaylistsFragment().getPlaylistsListView() != null)
    		playlistsListView.setAdapter(((ListeningRoom)getActivity()).getPlaylistsFragment().getPlaylistsListView().getAdapter());
    	playlistsListView.setOnItemClickListener(this);
    }


	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.newstation_cancel_button:
				getFragmentManager().popBackStack();
				break;
			case R.id.newstation_save_button:
				StationsFragment fragment = ((ListeningRoom)this.getActivity()).getStationsFragment();
				fragment.addStation(createStation());
				getFragmentManager().popBackStack();
				break;
			default:
				break;
		}
		
		if(v.getId() != R.id.newstation_name_input)
		{
			InputMethodManager inputMethodManager = (InputMethodManager) this.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(this.getActivity().getCurrentFocus().getWindowToken(), 0);
		}
	}
	
	private StationData createStation()
	{
		StationData returnStation = new StationData();
		EditText stationName = (EditText)this.getActivity().findViewById(R.id.newstation_name_input);
		
		returnStation.Name = stationName.getText().toString();
		returnStation.Owner = new UserData("CHANGETOCURRENTUSER", "");
		ListView lv = ((ListeningRoom)getActivity()).getPlaylistsFragment().getPlaylistsListView();
		PlayListAdapter playlistAdapter = (PlayListAdapter)lv.getAdapter();
		returnStation.Playlist = (PlaylistData)playlistAdapter.getItem(selectedPlaylistPos);
		returnStation.Song = new SongData();
		
		return returnStation;

	}


	@SuppressWarnings("deprecation")
	@Override
	public void onItemClick(AdapterView<?> av, View v, int i, long l) {
		
		av.getChildAt(selectedPlaylistPos).setBackgroundDrawable(v.getBackground());
		v.setBackgroundColor(getResources().getColor(R.color.holo_blue_light));
		selectedPlaylistPos = i;
	}

}
