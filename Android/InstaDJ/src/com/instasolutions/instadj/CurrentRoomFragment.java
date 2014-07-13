package com.instasolutions.instadj;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.instasolutions.instadj.util.ServicePostHelper;
import com.instasolutions.instadj.util.ServiceUploadHelper;

public class CurrentRoomFragment extends Fragment implements OnClickListener, OnSeekBarChangeListener {

	/** Called when the activity is first created. */
    private SeekBar seekbar;
    private MediaPlayer mediaplayer;
    private ImageButton btn_play;
    private ImageButton btn_next;
    private ImageButton btn_favourite;
    private ImageButton btn_like;
    private ImageButton btn_dislike;
    private ImageButton btn_removeRoom;
    private Handler updateHandler = new Handler();
    private TextView text_curTime;
    private TextView text_endTime;
    private TextView text_songName;
    private TextView text_artist;
    private TextView text_stationName;
    private ImageView large_art;
    private ImageView small_art;
    private double curTime = 0;
    private double endTime = 0;
    private Activity activity;
    private StationData station = null;
    private int playlistPosition = 0;
    private static Boolean initialized = false;
    private Boolean currentlyFavourited = false;
    private Boolean currentlyLiked = false;
    private Boolean currentlyDisliked = false;
    private Boolean firstSong = false;
    @Override
    public View onCreateView(LayoutInflater inflater, 
            ViewGroup container, 
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_room, container, false);
        activity = this.getActivity();
        activity.getActionBar().setTitle(R.string.app_name);
        this.setHasOptionsMenu(true);
        return view;
    }
    
    @Override
	public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
    	if(initialized == false)
    		initialize();
    	else
    		reinitialize();
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
    	inflater.inflate(R.menu.current_room, menu);
    	super.onCreateOptionsMenu(menu, inflater);
    }
    
    public void initialize()
    {
    	if(station == null)
    	{
    		 StationsFragment fragment = ((ListeningRoom)activity).getStationsFragment();
    		   FragmentManager fragmentManager = this.getFragmentManager();
    		   FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    	       fragmentTransaction.replace(R.id.container, fragment, "StationsFragment");
    	       fragmentTransaction.commit();
    	       AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity(), android.R.style.Theme_Holo_Dialog);
    	       builder.setMessage("Select a station.").setTitle("No Station");
    	       AlertDialog dialog = builder.create();
    	       dialog.show();
    	       return;
    	}
    	
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
    	Editor editPrefs = prefs.edit();
    	
    	if(station.Owner.getUserID().compareTo(prefs.getString("UserID", "0")) == 0)
    	{
    		editPrefs.putBoolean("userIsHosting", true);
    	}
    	else
    	{
    		editPrefs.putBoolean("userIsHosting", false);
    	}
    	
    	editPrefs.commit();
    	
    	station.Song = station.Playlist.Songs.get(playlistPosition);
		btn_play = (ImageButton)activity.findViewById(R.id.Button_Play);
		btn_next = (ImageButton)activity.findViewById(R.id.button_next);
	    btn_favourite = (ImageButton)activity.findViewById(R.id.room_favourite_button);
	    btn_like = (ImageButton)activity.findViewById(R.id.room_like_button);
	    btn_dislike = (ImageButton)activity.findViewById(R.id.room_dislike_button);
	    btn_removeRoom = (ImageButton)activity.findViewById(R.id.room_remove_button);
        seekbar = (SeekBar)activity.findViewById(R.id.seekBar1);
        text_curTime = (TextView)activity.findViewById(R.id.text_curTime);
        text_endTime = (TextView)activity.findViewById(R.id.text_endTime);
        text_songName = (TextView)activity.findViewById(R.id.text_song);
        text_artist = (TextView)activity.findViewById(R.id.text_artist);
        text_stationName = (TextView)activity.findViewById(R.id.currentroom_stationname_text);
        large_art = (ImageView)activity.findViewById(R.id.image_art);
        small_art = (ImageView)activity.findViewById(R.id.album_art);
        
        btn_play.setOnClickListener(this);
        btn_favourite.setOnClickListener(this);
        
        if(station != null)
        {
        	text_stationName.setText(station.Name);
        	text_songName.setText(station.Song.Title);
        	text_artist.setText(station.Song.Artist);
        	
        }
        if(prefs.getBoolean("userIsHosting", false))
        {
            seekbar.setOnSeekBarChangeListener(this);
            btn_next.setOnClickListener(this);
            btn_removeRoom.setOnClickListener(this);
        	btn_next.setVisibility(ImageView.VISIBLE);
        	btn_removeRoom.setVisibility(ImageView.VISIBLE);
        	
        	btn_like.setVisibility(ImageView.INVISIBLE);
        	btn_dislike.setVisibility(ImageView.INVISIBLE);
        	prepareRoom();
        }
        else
        {
        	btn_next.setVisibility(ImageView.INVISIBLE);
        	btn_removeRoom.setVisibility(ImageView.INVISIBLE);
        	
        	btn_like.setOnClickListener(this);
        	btn_dislike.setOnClickListener(this);
        	btn_like.setVisibility(ImageView.VISIBLE);
        	btn_dislike.setVisibility(ImageView.VISIBLE);
        	nextSong();
        }

    }
    
    public void reinitialize()
    {

    	
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

    	btn_play = (ImageButton)activity.findViewById(R.id.Button_Play);
		btn_next = (ImageButton)activity.findViewById(R.id.button_next);
	    btn_favourite = (ImageButton)activity.findViewById(R.id.room_favourite_button);
	    btn_like = (ImageButton)activity.findViewById(R.id.room_like_button);
	    btn_dislike = (ImageButton)activity.findViewById(R.id.room_dislike_button);
	    btn_removeRoom = (ImageButton)activity.findViewById(R.id.room_remove_button);
        seekbar = (SeekBar)activity.findViewById(R.id.seekBar1);
        text_curTime = (TextView)activity.findViewById(R.id.text_curTime);
        text_endTime = (TextView)activity.findViewById(R.id.text_endTime);
        text_songName = (TextView)activity.findViewById(R.id.text_song);
        text_artist = (TextView)activity.findViewById(R.id.text_artist);
        text_stationName = (TextView)activity.findViewById(R.id.currentroom_stationname_text);
        large_art = (ImageView)activity.findViewById(R.id.image_art);
        small_art = (ImageView)activity.findViewById(R.id.album_art);
        
        btn_play.setOnClickListener(this);
        btn_favourite.setOnClickListener(this);
        
        if(prefs.getBoolean("userIsHosting", false))
        {
            seekbar.setOnSeekBarChangeListener(this);
            btn_next.setOnClickListener(this);
        	btn_next.setVisibility(ImageView.VISIBLE);
        	btn_removeRoom.setVisibility(ImageView.VISIBLE);
        	
        	btn_like.setVisibility(ImageView.INVISIBLE);
        	btn_dislike.setVisibility(ImageView.INVISIBLE);
        }
        else
        {
        	btn_next.setVisibility(ImageView.INVISIBLE);
        	btn_removeRoom.setVisibility(ImageView.INVISIBLE);
        	
        	btn_like.setVisibility(ImageView.VISIBLE);
        	btn_dislike.setVisibility(ImageView.VISIBLE);
        }
        

        text_endTime.setText(String.valueOf(endTime));
        
        if(station != null)
        {
        	text_stationName.setText(station.Name);
        	text_songName.setText(station.Song.Title);
        	text_artist.setText(station.Song.Artist);
        	
        }

        if(mediaplayer.isPlaying())
    	{
    		btn_play.setImageResource(R.drawable.ic_action_pause);
    	}
    	else
    	{
	    	btn_play.setImageResource(R.drawable.ic_action_play);
    	}
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(station.Song.LocalPath);
        byte[] data = media.getEmbeddedPicture();
        if(data != null)
        {
        	Bitmap art_bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        	large_art.setImageBitmap(art_bitmap);	
        	small_art.setImageBitmap(art_bitmap);
        }
        
        seekbar.setMax((int)endTime);
    }
    
    public void setStation(StationData s)
    {
    	this.station = s;
    	this.station.Playlist.populateSongs();
    	if(mediaplayer != null)
    		mediaplayer.release();
    	if(updateHandler != null)
    		updateHandler.removeCallbacks(UpdateSeekBar);
    	if(text_stationName != null)
    	{
        	text_stationName.setText(station.Name);
    	}
    	initialized = false;

    }
	
	 public void play()
	    {
	    	if(mediaplayer.isPlaying())
	    	{
	    		mediaplayer.pause();
	    		btn_play.setImageResource(R.drawable.ic_action_play);
	    	}
	    	else
	    	{
		    	mediaplayer.start();
		    	btn_play.setImageResource(R.drawable.ic_action_pause);
		    	curTime = mediaplayer.getCurrentPosition();
		    	endTime = mediaplayer.getDuration();
		    	
		    	text_endTime.setText(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) endTime),
		    	         TimeUnit.MILLISECONDS.toSeconds((long) endTime) - 
		    	         TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) endTime))));
		    	text_curTime.setText(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) curTime),
		   	         TimeUnit.MILLISECONDS.toSeconds((long) curTime) - 
		   	         TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) curTime))));
		    	seekbar.setProgress((int)curTime);
		    	updateHandler.postDelayed(UpdateSeekBar, 100);
	    	}
	    }
	    
	    
	    private Runnable UpdateSeekBar = new Runnable(){
	    	public void run(){
	    		curTime = mediaplayer.getCurrentPosition();
	    		seekbar.setProgress((int)curTime);
	    		text_curTime.setText(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) curTime),
	    	   	         TimeUnit.MILLISECONDS.toSeconds((long) curTime) - 
	    	   	         TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) curTime))));
	    		updateHandler.postDelayed(this, 900);
	    		//Song Complete
	    		if(curTime >= endTime)
	    		{
	    			nextSong();
	    		}
	    	}
	    };
	    
	    private void nextSong()
	    {
	    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
	    	if(mediaplayer != null)
	    		mediaplayer.release();
	    	//Add favorited song to database
	    	if(currentlyFavourited)
	    	{
	    		ServicePostHelper helper = new ServicePostHelper();
	    		helper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
	    				"http://instadj.amir20001.cloudbees.net/favourite/insert/" + prefs.getString("UserID", "0") + "/" + station.Song.id);
	    	}
	    	//Remove previous song from storage
	    	if(prefs.getBoolean("userIsHosting", false) && !firstSong)
	    	{
	    		ServicePostHelper helper = new ServicePostHelper();
	    		helper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
	    				"http://instadj.amir20001.cloudbees.net/song/delete/" + station.Song.id);
	    	}
	    	if(!initialized)
	    	{
	    		initialized = true;
	    	}
	    	else
	    	{
	    		((RightDrawerFragment)getFragmentManager().findFragmentById(R.id.navigation_drawer_right)).updateSongHistory(station.Song);
	    	}
	    	
	    	if(playlistPosition >= station.Playlist.Songs.size())
	    	{
	    		playlistPosition = 0;
	    	}
	    	station.Song = station.Playlist.Songs.get(playlistPosition);
	    	//Upload next song
	    	ServiceUploadHelper upload = new ServiceUploadHelper();
	    	upload.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
    				station.Playlist.Songs.get(playlistPosition+1 > station.Playlist.Songs.size() ? 0 : playlistPosition+1 ));

    	    Uri.Builder uri_b = new Uri.Builder();
    	    
	    	if(prefs.getBoolean("userIsHosting", false)){
	            uri_b.path(station.Song.LocalPath);
	    	}
	    	else
	    	{
	    		uri_b.path(station.Song.Song_URL);
	    	}
	    	mediaplayer = MediaPlayer.create(activity, uri_b.build());
	    	//Async task currently takes some time to fire update the art to blank until it does
	    	large_art.setImageResource(R.drawable.blankart);
	    	small_art.setImageResource(R.drawable.blankart);
	    	final GettArtworkTask task1 = new GettArtworkTask(large_art);
			task1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
    				station.Song.Art_URL);
			final GettArtworkTask task2 = new GettArtworkTask(small_art);
			task2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
    				station.Song.Art_URL);
	    	text_songName.setText(station.Song.Title);
	        text_artist.setText(station.Song.Artist);
    		endTime = mediaplayer.getDuration();
    		seekbar.setMax((int)endTime);
    		firstSong = false;
    		
    		currentlyFavourited = false;
    		currentlyDisliked = false;
    		currentlyLiked = false;
    		btn_like.setImageResource(R.drawable.ic_action_good);
    		btn_dislike.setImageResource(R.drawable.ic_action_bad);
    		btn_favourite.setImageResource(R.drawable.ic_action_favorite);
	        play();
	    	playlistPosition++;
	    }
	    
	    private void prepareRoom()
	    {
	    	final ProgressDialog dialog = ProgressDialog.show(this.activity, "Preparing Room", "Please Wait...", true);
	    	final CurrentRoomFragment room = this;
	    	dialog.show();
	    	ServiceUploadHelper upload = new ServiceUploadHelper(){
	    		@Override
	    		 protected void onPostExecute(Integer i) {
	    	    	dialog.dismiss();
	    	    	room.nextSong();
	    		}
	    	};
	    	upload.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
    				station.Song);
	    	firstSong = true;

	    	
	    }
	    
	    private void updateLikes(Boolean like)
	    {
	    	if(like)
	    	{
	    		if(currentlyLiked)
	    		{
	    			currentlyLiked = false;
	    			btn_like.setImageResource(R.drawable.ic_action_good);
	    		}
	    		else
	    		{
	    			currentlyLiked = true;
		    		btn_like.setImageResource(R.drawable.ic_action_good_green);
		    		btn_dislike.setImageResource(R.drawable.ic_action_bad);
	    		}
	    	}
	    	else
	    	{
	    		if(currentlyDisliked)
	    		{
	    			currentlyDisliked = false;
	    			btn_dislike.setImageResource(R.drawable.ic_action_bad);
	    		}
	    		else
	    		{
	    			currentlyDisliked = true;
	    			btn_like.setImageResource(R.drawable.ic_action_good);
	    			btn_dislike.setImageResource(R.drawable.ic_action_bad_red);
	    		}
	    		
	    	}
	    }
	    
	    private void updateFavourites()
	    {
	    	if(currentlyFavourited)
	    	{
	    		btn_favourite.setImageResource(R.drawable.ic_action_favorite);
	    		currentlyFavourited = false;
	    	}
	    	else
	    	{
	    		btn_favourite.setImageResource(R.drawable.ic_action_favorite_red);
	    		currentlyFavourited = true;
	    	}
	    }
	    
	    private void closeRoom()
	    {
	    	
	    }
	    

		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
				case R.id.Button_Play:
					play();
					break;
				case R.id.button_next:
					nextSong();
					break;
				case R.id.room_dislike_button:
					updateLikes(false);
					break;
				case R.id.room_favourite_button:
					updateFavourites();
					break;
				case R.id.room_like_button:
					updateLikes(true);
					break;
				case R.id.room_remove_button:
					closeRoom();
					break;
				default:
					break;
			}
			
		}

		@Override
		public void onProgressChanged(SeekBar s, int i, boolean userInput) {
			if(userInput)
			{
				mediaplayer.seekTo(i);
				s.setProgress(i);	
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}

};
