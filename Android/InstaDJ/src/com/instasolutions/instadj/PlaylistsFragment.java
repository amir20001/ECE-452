package com.instasolutions.instadj;

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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class PlaylistsFragment extends Fragment implements OnClickListener, OnItemClickListener, OnItemLongClickListener{
	
	SparseArray<PlaylistData> playlists = new SparseArray<PlaylistData>();
	ListView lv = null;
	int activeItem = 0;

	/** Called when the activity is first created. */
	@Override
	 public View onCreateView(LayoutInflater inflater, 
	            ViewGroup container, 
	            Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.fragment_playlists, container, false);
	        return view;
	    }
	
    @Override
	public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
    	
    	Button NewPlaylistButton = (Button)this.getActivity().findViewById(R.id.playlists_new_button);
    	NewPlaylistButton.setOnClickListener(this);
    	
        playlists.append(0, new PlaylistData("Playlist1", "Pop", 10));
        playlists.append(1, new PlaylistData("Playlist2", "Mix", 50));
        PlayListAdapter adapter = new PlayListAdapter(this.getActivity(), playlists );
        adapter.setButtonsEnabled(true);
        lv = (ListView)this.getActivity().findViewById(R.id.playlists_list);
        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);
        lv.setAdapter(adapter);
		
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
