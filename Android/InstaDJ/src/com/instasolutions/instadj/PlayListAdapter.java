package com.instasolutions.instadj;



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.instasolutions.instadj.util.ServicePostHelper;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayListAdapter extends BaseAdapter{

	private Activity activity;
	private SparseArray<PlaylistData> playlists = new SparseArray<PlaylistData>();
	private static LayoutInflater inflater = null;
	private boolean useButtons = false;
	
	public PlayListAdapter(Activity a, SparseArray<PlaylistData> d)
	{
		activity = a;
		playlists = d;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		
		return playlists.size();
	}

	@Override
	public Object getItem(int pos) {
		return playlists.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View view, ViewGroup viewgroup) {
		View v = view;
		if(view==null)
			v = inflater.inflate(R.layout.list_row_playlists, null);
		
		final TextView PlaylistName = (TextView)v.findViewById(R.id.playlist_name);
		TextView PlaylistGenre = (TextView)v.findViewById(R.id.playlist_genre);
		TextView TrackCount = (TextView)v.findViewById(R.id.playlsit_tracks);
		ImageButton deleteButton = (ImageButton)v.findViewById(R.id.playlist_delete_button);
			
		final PlaylistData playlist = playlists.get(pos);
		
		PlaylistName.setText(playlist.Name);
		PlaylistGenre.setText(playlist.Genre);
		TrackCount.setText(String.valueOf(playlist.TrackCount));
		if(!useButtons)
		{
			deleteButton.setVisibility(ImageView.INVISIBLE);
		}
		else
		{
			deleteButton.setOnClickListener(new OnClickListener() { 
		        @Override 
		        public void onClick(View v) { 
		        	ServicePostHelper helper = new ServicePostHelper();
		        	helper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
		        			"http://instadj.amir20001.cloudbees.net/playlist/delete/" + playlist.id);
		        	try {
						helper.get();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
		        	
		        	PlaylistsFragment currentFragment = ((ListeningRoom)activity).getPlaylistsFragment();
		        	FragmentActivity fragActivity = (FragmentActivity)activity;
		            FragmentTransaction fragTransaction = fragActivity.getSupportFragmentManager().beginTransaction();
		            fragTransaction.detach(currentFragment);
		            fragTransaction.attach(currentFragment);
		            fragTransaction.commit();
		        } 
		    }); 
		}
				
		
		return v;
	}
	
	public void setButtonsEnabled(boolean enabled){
		useButtons = enabled;
	}

}
