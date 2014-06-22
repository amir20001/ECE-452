package com.instasolutions.instadj;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.content.CursorLoader;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public class NewPlaylistFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor>, OnClickListener{

	private static final int URL_LOADER = 0;
    String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
    String[] projection = {
    		MediaStore.Audio.Media.TITLE,
    		MediaStore.Audio.Media.ARTIST,
    		MediaStore.Audio.Media.ALBUM,
    		MediaStore.Audio.Media.DURATION,
    		MediaStore.Audio.Media.DATA
    };
    ListView listview = null;
    SongListAdapter songlist_adapter = null;
	
	/** Called when the activity is first created. */
	@Override
	 public View onCreateView(LayoutInflater inflater, 
	            ViewGroup container, 
	            Bundle savedInstanceState) {

	        View view = inflater.inflate(R.layout.fragment_newplaylist, container, false);
	    
	        
	        getLoaderManager().initLoader(URL_LOADER, null, this);
	        
	        return view;
	    }
	
    @Override
	public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
		
    	Button cancelButton = (Button)this.getActivity().findViewById(R.id.newplaylist_cancel_button);
    	Button saveButton = (Button)this.getActivity().findViewById(R.id.newplaylist_save_button);
    	cancelButton.setOnClickListener(this);
    	saveButton.setOnClickListener(this);
    	
    }

	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
		
		 switch (loaderID) {
	        case URL_LOADER:
	            // Returns a new CursorLoader
	            return new CursorLoader(
	                        getActivity(),   // Parent activity context
	                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,        // Table to query
	                        projection,     // Projection to return
	                        selection,            // No selection clause
	                        null,            // No selection arguments
	                        null             // Default sort order
	        );
	        default:
	            // An invalid id was passed in
	            return null;
	    }
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		
        listview = (ListView)this.getActivity().findViewById(R.id.newplaylist_songlist);
        SparseArray<SongData> songs = new SparseArray<SongData>();
        for(int i = 0; cursor.moveToNext(); cursor.moveToNext(), i++){
        	Integer durLong = Integer.valueOf(cursor.getString(3));
        	String durString = cursor.getString(3);
        	if(durLong != null)
        	{
	        	durString = String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(durLong),
			    	         TimeUnit.MILLISECONDS.toSeconds(durLong) - 
			    	         TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(durLong)));
        	}
        	songs.append(i, new SongData(this.getActivity(), cursor.getString(0), cursor.getString(1), 
        				cursor.getString(2), durString, cursor.getString(4)));
        }
        songlist_adapter = new SongListAdapter(this.getActivity(), songs, true);
        listview.setAdapter(songlist_adapter);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.newplaylist_cancel_button:
				getFragmentManager().popBackStack();
				break;
			case R.id.newplaylist_save_button:
				PlaylistsFragment fragment = ((ListeningRoom)this.getActivity()).getPlaylistsFragment();
				fragment.addPlaylist(createPlaylist());
				getFragmentManager().popBackStack();
				break;
			default:
				break;
		}
		
		if(v.getId() != R.id.newplaylist_name_input)
		{
			InputMethodManager inputMethodManager = (InputMethodManager) this.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(this.getActivity().getCurrentFocus().getWindowToken(), 0);
		}
	}
	
	private PlaylistData createPlaylist()
	{
		PlaylistData returnPlaylist = new PlaylistData();
		for(int i = 0, c = 0; i < listview.getCount();i++)
		{
			if(songlist_adapter.getCheckBoxArray().get(i))
			{
				SongListAdapter view = (SongListAdapter)listview.getAdapter();
				returnPlaylist.Songs.append(c, (SongData)view.getItem(i));
				c++;
			}
		}
		EditText playlistName = (EditText)this.getActivity().findViewById(R.id.newplaylist_name_input);
		Spinner playlistGenre = (Spinner)this.getActivity().findViewById(R.id.newplaylist_genre_spinner);
		
		returnPlaylist.Name = playlistName.getText().toString();
		returnPlaylist.Genre = playlistGenre.getSelectedItem().toString();
		returnPlaylist.TrackCount = returnPlaylist.Songs.size();
		return returnPlaylist;

	}

}
