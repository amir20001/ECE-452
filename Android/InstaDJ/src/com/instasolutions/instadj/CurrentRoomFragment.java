package com.instasolutions.instadj;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class CurrentRoomFragment extends Fragment {

	/** Called when the activity is first created. */
    private SeekBar seekbar;
    private MediaPlayer mediaplayer;
    private ImageButton btn_play;
    private Handler updateHandler = new Handler();
    private TextView text_curTime;
    private TextView text_endTime;
    private TextView text_songName;
    private TextView text_artist;
    private TextView text_stationName;
    private ImageView large_art;
    private ImageView small_art;
    private int firstPlay = 1;
    private double curTime = 0;
    private double endTime = 0;
    private String path = "/storage/sdcard1/Music/01 Danza Kuduro.mp3";
    private Activity activity;
    private StationData station = null;
    
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
		initialize();
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
    	inflater.inflate(R.menu.current_room, menu);
    	super.onCreateOptionsMenu(menu, inflater);
    }
    
    public void initialize()
    {
	    Uri.Builder uri_b = new Uri.Builder();
//        mediaplayer = MediaPlayer.create(activity, uri_b.path(path).build());
//		btn_play = (ImageButton)activity.findViewById(R.id.Button_Play);
//        seekbar = (SeekBar)activity.findViewById(R.id.seekBar1);
//        text_curTime = (TextView)activity.findViewById(R.id.text_curTime);
//        text_endTime = (TextView)activity.findViewById(R.id.text_endTime);
//        text_songName = (TextView)activity.findViewById(R.id.text_song);
//        text_artist = (TextView)activity.findViewById(R.id.text_artist);
//        text_stationName = (TextView)activity.findViewById(R.id.currentroom_stationname_text);
//        large_art = (ImageView)activity.findViewById(R.id.image_art);
//        small_art = (ImageView)activity.findViewById(R.id.album_art);
//        MediaMetadataRetriever media = new MediaMetadataRetriever();
//        media.setDataSource(path);
//        text_songName.setText(media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
//        text_artist.setText(media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
//        byte[] data = media.getEmbeddedPicture();
//        if(data != null)
//        {
//        	Bitmap art_bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//        	large_art.setImageBitmap(art_bitmap);	
//        	small_art.setImageBitmap(art_bitmap);
//        }
        
        if(station != null)
        {
        	text_stationName.setText(station.Name);
        	text_songName.setText(station.Song.Title);
        	text_artist.setText(station.Song.Artist);
        }
    }
    
    public void setStation(StationData s)
    {
    	this.station = s;
    }
	
	 public void play(View view)
	    {
	    	
	    	if(firstPlay == 1)
	    	{
	    		endTime = mediaplayer.getDuration();
	    		seekbar.setMax((int)endTime);
	    		firstPlay = 0;
	    	}
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
	    	}
	    };

};
