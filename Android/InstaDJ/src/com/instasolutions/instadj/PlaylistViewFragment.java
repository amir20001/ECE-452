package com.instasolutions.instadj;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

public class PlaylistViewFragment extends Fragment {
	
	private PlaylistData mPlaylist = new PlaylistData();
	
	/** Called when the activity is first created. */
	@Override
	 public View onCreateView(LayoutInflater inflater, 
	            ViewGroup container, 
	            Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
	        return view;
	    }
	
	 @Override
	 public void onActivityCreated(Bundle savedInstanceState)
	 {
		 super.onActivityCreated(savedInstanceState);
		 ListView songListView = (ListView)this.getActivity().findViewById(R.id.playlist_song_list);
		 ProgressBar pbar = (ProgressBar)this.getActivity().findViewById(R.id.playlist_progressbar);
		 pbar.setVisibility(ImageView.VISIBLE);
		 mPlaylist.populateSongs();
		 pbar.setVisibility(ImageView.INVISIBLE);
		 SongListAdapter adapter = new SongListAdapter(this.getActivity(), mPlaylist.Songs);
		 songListView.setAdapter(adapter);
	 }
	
	public void setPlaylist(PlaylistData playlist)
	{ 
		mPlaylist = playlist;
	}

}
