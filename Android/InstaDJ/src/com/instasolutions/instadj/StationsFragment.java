package com.instasolutions.instadj;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class StationsFragment extends ListFragment {

	/** Called when the activity is first created. */
	@Override
	 public View onCreateView(LayoutInflater inflater, 
	            ViewGroup container, 
	            Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.fragment_stations, container, false);
	        String[] values = new String[] {"Station 1", "Station 2", "Station 3"};
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
	        setListAdapter(adapter);
	        return view;
	    }

}
