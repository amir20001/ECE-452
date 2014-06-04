package com.instasolutions.instadj;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class CurrentRoomFragment extends Fragment {

	/** Called when the activity is first created. */
    private SeekBar seekbar;
    private MediaPlayer mediaplayer;
    private Button btn_play;
    private Handler updateHandler = new Handler();
    private TextView text_curTime;
    private TextView text_endTime;
    private ImageView image_art;
    private int firstPlay = 1;
    private double curTime = 0;
    private double endTime = 0;
    private String path = "/storage/sdcard1/Music/01 Danza Kuduro.mp3";
    private Activity activity;
    
    @Override
    public View onCreateView(LayoutInflater inflater, 
            ViewGroup container, 
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_room, container, false);
        activity = this.getActivity();
        return view;
    }
    
    @Override
	public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
		initialize();
    }
    
    public void initialize()
    {
	    Uri.Builder uri_b = new Uri.Builder();
        mediaplayer = MediaPlayer.create(activity, uri_b.path(path).build());
		btn_play = (Button)activity.findViewById(R.id.Button_Play);
        seekbar = (SeekBar)activity.findViewById(R.id.seekBar1);
        text_curTime = (TextView)activity.findViewById(R.id.text_curTime);
        text_endTime = (TextView)activity.findViewById(R.id.text_endTime);
        image_art = (ImageView)activity.findViewById(R.id.image_art);
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        byte[] data = media.getEmbeddedPicture();
        if(data != null)
        {
        	Bitmap art_bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        	image_art.setImageBitmap(art_bitmap);	
        }
    }
	
	 public void play(View view)
	    {
	    	
	    	if(firstPlay == 1)
	    	{
	    		seekbar.setMax((int)endTime);
	    		firstPlay = 0;
	    	}
	    	if(mediaplayer.isPlaying())
	    	{
	    		mediaplayer.pause();
	    		btn_play.setText("Play");
	    	}
	    	else
	    	{
		    	mediaplayer.start();
		    	btn_play.setText("Pause");
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
