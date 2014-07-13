package com.instasolutions.instadj;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class ListeningRoom extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, RightDrawerFragment.NavigationDrawerCallbacks{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment_left;
    private RightDrawerFragment mNavigationDrawerFragment_right;
    
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private CurrentRoomFragment curRoomFrag;
    private StationsFragment stationsFrag;
    private FavoritesFragment favoritesFrag;
    private PlaylistsFragment playlistsFrag;
    private FollowingFragment followingFragment;
    private FollowersFragment followersFragment;
    private ProfileFragment profileFrag;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        curRoomFrag = new CurrentRoomFragment();
        stationsFrag = new StationsFragment();
        favoritesFrag = new FavoritesFragment();
        playlistsFrag = new PlaylistsFragment();
        followingFragment = new FollowingFragment();
        followersFragment = new FollowersFragment();
        profileFrag = new ProfileFragment();
        setContentView(R.layout.activity_listening_room);
        mNavigationDrawerFragment_left = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_left);
        mTitle = getTitle();

        // Set up the left drawer.
        mNavigationDrawerFragment_left.setUp(
                R.id.navigation_drawer_left,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mNavigationDrawerFragment_right = (RightDrawerFragment)
        		getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_right);
        // Set up the right drawer        
        mNavigationDrawerFragment_right.setUp(
                R.id.navigation_drawer_right,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment selectedFragment = new Fragment();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        switch(position)
        {
        case 0: 
        	selectedFragment = curRoomFrag;
        	break;
        case 1:
        	if(!prefs.getBoolean("userIsHosting", false)){
        		selectedFragment = stationsFrag;
        	}
        	else
        	{
        		AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Dialog);
       	       builder.setMessage("Close your room before selecting a new station.").setTitle("Currenty Hosting");
       	       AlertDialog dialog = builder.create();
       	       dialog.show();
       	       return;
        	}
        	break;
        case 2:
        	selectedFragment = favoritesFrag;
        	break;
        case 3:
        	selectedFragment = playlistsFrag;
        	break;
        case 4:
            selectedFragment = followingFragment;
            break;
        case 5:
            selectedFragment = followersFragment;
            break;
        case 6:
        	selectedFragment = profileFrag;
        	break;
        default: 
        	selectedFragment = stationsFrag;
        	break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, selectedFragment)
                .commit();
        
    }
    
    @Override
    public void onRightDrawerItemSelected(int position) {
        // update the main content by replacing fragment
        
    }


    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment_left.isDrawerOpen()) {
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
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onDestroy()
    {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	if(prefs.getBoolean("userIsHosting", false)){
    		//TODO: Add close room code here as well
    		Editor prefEdit = prefs.edit();
    		prefEdit.putBoolean("userIsHosting", false);
    		prefEdit.commit();
    	}
    	super.onDestroy();
    }
    
    public PlaylistsFragment getPlaylistsFragment()
    {
    	return playlistsFrag;
    }
    
    public CurrentRoomFragment getCurrentRoomFragment()
    {
    	return curRoomFrag;
    }
    
    public StationsFragment getStationsFragment()
    {
    	return stationsFrag;
    }
    
    
   
};


