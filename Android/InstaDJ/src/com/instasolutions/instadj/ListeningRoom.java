package com.instasolutions.instadj;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class ListeningRoom extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_room);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        Uri.Builder uri_b = new Uri.Builder();
        mediaplayer = MediaPlayer.create(this, uri_b.path(path).build());
        
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
        
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.listening_room, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void play(View view)
    {
    	
    	if(firstPlay == 1)
    	{
    		btn_play = (Button)findViewById(R.id.Button_Play);
            seekbar = (SeekBar)findViewById(R.id.seekBar1);
            text_curTime = (TextView)findViewById(R.id.text_curTime);
            text_endTime = (TextView)findViewById(R.id.text_endTime);
            image_art = (ImageView)findViewById(R.id.image_art);
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(path);
            byte[] data = media.getEmbeddedPicture();
            if(data != null)
            {
            	Bitmap art_bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            	image_art.setImageBitmap(art_bitmap);	
            }
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
    
    public void pause(View view)
    {
    	mediaplayer.pause();
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_listening_room, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((ListeningRoom) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
