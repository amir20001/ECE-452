package com.instasolutions.instadj;

import java.util.concurrent.TimeUnit;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.content.CursorLoader;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public class NewPlaylistFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor>{

	private static final int URL_LOADER = 0;
    String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
    String[] projection = {
    		MediaStore.Audio.Media.TITLE,
    		MediaStore.Audio.Media.ARTIST,
    		MediaStore.Audio.Media.ALBUM,
    		MediaStore.Audio.Media.DURATION
    };
	
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
		
        ListView listview = (ListView)this.getActivity().findViewById(R.id.newplaylist_songlist);
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
        	songs.append(i, new SongData(this.getActivity(), cursor.getString(0), cursor.getString(1), cursor.getString(2), durString, ""));
        }
        SongListAdapter adapter = new SongListAdapter(this.getActivity(), songs, R.layout.list_row_songs_select);
        listview.setAdapter(adapter);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		
	}

}
