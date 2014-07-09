package com.instasolutions.instadj;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

public class CurrentRoomFragment extends Fragment implements OnClickListener, OnSeekBarChangeListener {

	/** Called when the activity is first created. */
    private SeekBar seekbar;
    private MediaPlayer mediaplayer;
    private ImageButton btn_play;
    private ImageButton btn_next;
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
    
    @Override
    public View onCreateView(LayoutInflater inflater, 
            ViewGroup container, 
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_room, container, false);
        activity = this.getActivity();
        this.setHasOptionsMenu(true);
        return view;
    }
    
    @Override
	public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
    	if(initialized == false)
    		initialize();
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
    	       fragmentTransaction.replace(R.id.container, fragment);
    	       fragmentTransaction.commit();
    	       AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity(), android.R.style.Theme_Holo_Dialog);
    	       builder.setMessage("Select a station.").setTitle("No Station");
    	       AlertDialog dialog = builder.create();
    	       dialog.show();
    	       return;
    	}
    	station.Song = station.Playlist.Songs.get(playlistPosition);
		btn_play = (ImageButton)activity.findViewById(R.id.Button_Play);
		btn_next = (ImageButton)activity.findViewById(R.id.button_next);
        seekbar = (SeekBar)activity.findViewById(R.id.seekBar1);
        text_curTime = (TextView)activity.findViewById(R.id.text_curTime);
        text_endTime = (TextView)activity.findViewById(R.id.text_endTime);
        text_songName = (TextView)activity.findViewById(R.id.text_song);
        text_artist = (TextView)activity.findViewById(R.id.text_artist);
        text_stationName = (TextView)activity.findViewById(R.id.currentroom_stationname_text);
        large_art = (ImageView)activity.findViewById(R.id.image_art);
        small_art = (ImageView)activity.findViewById(R.id.album_art);
        
        btn_play.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        seekbar.setOnSeekBarChangeListener(this);
        
        if(station != null)
        {
        	text_stationName.setText(station.Name);
        }
        
        nextSong();
    }
    
    public void setStation(StationData s)
    {
    	this.station = s;
    	if(text_stationName != null)
    	{
        	text_stationName.setText(station.Name);
    	}

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
	    		updateHandler.postDelayed(this, 100);
	    		if(curTime >= endTime)
	    		{
	    			nextSong();
	    		}
	    	}
	    };
	    
	    private void nextSong()
	    {
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
	    	if(mediaplayer != null)
	    		mediaplayer.release();
    	    Uri.Builder uri_b = new Uri.Builder();
            mediaplayer = MediaPlayer.create(activity, uri_b.path(station.Song.LocalPath).build());
    		endTime = mediaplayer.getDuration();
    		seekbar.setMax((int)endTime);
	        MediaMetadataRetriever media = new MediaMetadataRetriever();
	        media.setDataSource(station.Song.LocalPath);
	        text_songName.setText(media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
	        text_artist.setText(media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
	        byte[] data = media.getEmbeddedPicture();
	        if(data != null)
	        {
	        	Bitmap art_bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
	        	large_art.setImageBitmap(art_bitmap);	
	        	small_art.setImageBitmap(art_bitmap);
	        }
	        play();
	    	playlistPosition++;
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
