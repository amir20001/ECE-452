package com.instasolutions.instadj;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.Toast;

import com.instasolutions.instadj.util.ServicePostHelper;

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
    	Spinner genreSpinner = (Spinner)this.getActivity().findViewById(R.id.newplaylist_genre_spinner);
    	
    	cancelButton.setOnClickListener(this);
    	saveButton.setOnClickListener(this);
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_genre_view, getResources().getStringArray(R.array.genres_array));
    	genreSpinner.setAdapter(adapter);
    	
    	EditText filter = (EditText)this.getActivity().findViewById(R.id.newplaylist_filter_songs);
    	filter.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
				songlist_adapter.getFilter().filter(s);
				
			}
    		
    	});
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
        	songs.append(i, new SongData(-1, cursor.getString(0), cursor.getString(1), 
        				cursor.getString(2), durString, cursor.getString(4), "", 0));
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
				EditText playlistName = (EditText)this.getActivity().findViewById(R.id.newplaylist_name_input);
				String name = playlistName.getText().toString();
				if(name.length() == 0)
				{
					Toast.makeText(this.getActivity(), "Enter a playlist name.", Toast.LENGTH_SHORT).show();
				}
				else if(name.length() > 15)
				{
					Toast.makeText(this.getActivity(), "Playlist name must be under 15 character.", Toast.LENGTH_SHORT).show();
				}
				else if(songlist_adapter.getCheckBoxArray().size() < 1)
				{
					Toast.makeText(this.getActivity(), "Select at least one song.", Toast.LENGTH_LONG).show();
				}
				else
				{
					createPlaylist();
					getFragmentManager().popBackStack();
				}
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
		for(int i = 0, c = 0; i < songlist_adapter.getCount();i++)
		{
			if(songlist_adapter.getCheckBoxArray().get(i))
			{
				returnPlaylist.Songs.append(c, (SongData)songlist_adapter.getItem(i));
				c++;
			}
		}
		EditText playlistName = (EditText)this.getActivity().findViewById(R.id.newplaylist_name_input);
		Spinner playlistGenre = (Spinner)this.getActivity().findViewById(R.id.newplaylist_genre_spinner);
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
		
		returnPlaylist.Name = playlistName.getText().toString();
		returnPlaylist.Genre = playlistGenre.getSelectedItem().toString();
		returnPlaylist.TrackCount = returnPlaylist.Songs.size();
    	JSONObject jplaylist = new JSONObject();
		try{
        	jplaylist.put("name", returnPlaylist.Name);
        	jplaylist.put("genre", returnPlaylist.Genre);
        	jplaylist.put("trackCount", returnPlaylist.TrackCount);
        	jplaylist.put("userId", prefs.getString("UserID", "0"));
        	ServicePostHelper post = new ServicePostHelper();
        	post.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
    				"http://instadj.amir20001.cloudbees.net/playlist/insert",jplaylist.toString());
        	String result = post.get();
        	jplaylist = new JSONObject(result);
        }catch (Exception e){
        	Log.e("instaDJ", "JSONException", e);
        }
		try{
			JSONArray jSongsArray = new JSONArray();
			for(int i = 0; i < returnPlaylist.Songs.size(); i++){
				SongData song = returnPlaylist.Songs.get(i);
				JSONObject jSong = new JSONObject();
				jSong.put("title", song.Title);
				jSong.put("artist", song.Artist);
				jSong.put("album", song.Album);
				jSong.put("duration", song.Duration);
				jSong.put("playlistId",jplaylist.getInt("id"));
				jSong.put("songUri", song.LocalPath);
				jSongsArray.put(jSong.toString());
			}
	    	ServicePostHelper post = new ServicePostHelper();
			post.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
    				"http://instadj.amir20001.cloudbees.net/song/insertmultiple", jSongsArray.toString());
		}catch (Exception e){
        	Log.e("instaDJ", "JSONException", e);
        }
		
       
		
		return returnPlaylist;

	}

}
