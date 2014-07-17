package com.instasolutions.instadj;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.instasolutions.instadj.util.ServiceGetHelper;
import com.instasolutions.instadj.util.ServicePostHelper;

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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class ListeningRoom extends FragmentActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks,
		RightDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private final String SENDER_ID = "31805204739";
	private NavigationDrawerFragment mNavigationDrawerFragment_left;
	private RightDrawerFragment mNavigationDrawerFragment_right;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private CurrentRoomFragment curRoomFrag;
	private StationsFragment stationsFrag;
	private FavoritesFragment favoritesFrag;
	private PlaylistsFragment playlistsFrag;
	private FollowingFragment followingFragment;
	private FollowersFragment followersFragment;
	private ProfileFragment profileFrag;
	private Context context;

	public static ListeningRoom mThis = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mThis = this;
		curRoomFrag = new CurrentRoomFragment();
		stationsFrag = new StationsFragment();
		playlistsFrag = new PlaylistsFragment();
		favoritesFrag = new FavoritesFragment();
		followingFragment = new FollowingFragment();
		followersFragment = new FollowersFragment();
		profileFrag = new ProfileFragment();
		context = this.getApplicationContext();
		setContentView(R.layout.activity_listening_room);
		mNavigationDrawerFragment_left = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer_left);
		mTitle = getTitle();
		// Set up the left drawer.
		mNavigationDrawerFragment_left.setUp(R.id.navigation_drawer_left,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		mNavigationDrawerFragment_right = (RightDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer_right);
		// Set up the right drawer
		mNavigationDrawerFragment_right.setUp(R.id.navigation_drawer_right,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editPrefs = prefs.edit();
		editPrefs.putBoolean("userIsHosting", false);
		editPrefs.commit();

		verifyGCMId();

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment selectedFragment = new Fragment();
		String fragmentTag = "";
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		switch (position) {
		case 0:
			selectedFragment = curRoomFrag;
			fragmentTag = "CurrentRoomFragment";
			break;
		case 1:
			if (!prefs.getBoolean("userIsHosting", false)) {
				selectedFragment = stationsFrag;
				fragmentTag = "StationsFragment";
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						new ContextThemeWrapper(this,
								android.R.style.Theme_Holo_Dialog));
				builder.setMessage(
						"Close your room before selecting a new station.")
						.setTitle("Currenty Hosting");
				AlertDialog dialog = builder.create();
				dialog.show();
				return;
			}
			break;
		case 2:
			if (!prefs.getBoolean("userIsHosting", false)) {
				selectedFragment = playlistsFrag;
				fragmentTag = "PlaylistsFragment";
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(this,
						android.R.style.Theme_Holo_Dialog);
				builder.setMessage(
						"Close your room before modifying playlists.")
						.setTitle("Currenty Hosting");
				AlertDialog dialog = builder.create();
				dialog.show();
				return;
			}
			break;
		case 3:
			selectedFragment = favoritesFrag;
			fragmentTag = "FavoritesFragment";
			break;
		case 4:
			selectedFragment = followingFragment;
			fragmentTag = "FollowingFragment";
			break;
		case 5:
			selectedFragment = followersFragment;
			fragmentTag = "FollowersFragment";
			break;
		case 6:
			selectedFragment = profileFrag;
			fragmentTag = "ProfileFragment";
			break;
		default:
			selectedFragment = stationsFrag;
			fragmentTag = "StationsFragment";
			break;
		}
		fragmentManager.beginTransaction()
				.replace(R.id.container, selectedFragment, fragmentTag)
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
	protected void onDestroy() {
		this.getCurrentRoomFragment().closeRoom();
		super.onDestroy();
	}

	public PlaylistsFragment getPlaylistsFragment() {
		return playlistsFrag;
	}

	public CurrentRoomFragment getCurrentRoomFragment() {
		return curRoomFrag;
	}

	public StationsFragment getStationsFragment() {
		return stationsFrag;
	}

	public FavoritesFragment getFavoritesFragment() {
		return favoritesFrag;
	}

	public void verifyGCMId() {
		// check to see if user has a registered GCM
		ServiceGetHelper helper = new ServiceGetHelper() {
			@Override
			protected void onPostExecute(String result) {
				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(context);

				JSONObject jUser = new JSONObject();
				String GCMId = "";

				try {
					jUser = new JSONObject(result);
					GCMId = jUser.getString("gcmId");
				} catch (JSONException e1) {

					e1.printStackTrace();
				}

				if (GCMId.isEmpty() || GCMId.compareTo("null") == 0) {
					registerGCMInBackground();
				} else {
					Editor prefEdit = prefs.edit();
					prefEdit.putString("GCMID", GCMId);
					prefEdit.commit();
				}

			}
		};
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		helper.executeOnExecutor(
				AsyncTask.THREAD_POOL_EXECUTOR,
				"http://instadj.amir20001.cloudbees.net/user/get/"
						+ prefs.getString("UserID", "UserID"));

	}

	private void registerGCMInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String regid = "";
				try {
					GoogleCloudMessaging gcm = GoogleCloudMessaging
							.getInstance(context);
					SharedPreferences prefs = PreferenceManager
							.getDefaultSharedPreferences(context);
					regid = gcm.register(SENDER_ID);

					ServicePostHelper helper = new ServicePostHelper();
					helper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
							"http://instadj.amir20001.cloudbees.net/user/updategcmid/"
									+ prefs.getString("UserID", "UserID") + "/"
									+ regid);
					Editor prefEdit = prefs.edit();
					prefEdit.putString("GCMID", regid);
					prefEdit.commit();
					return regid;

				} catch (IOException ex) {
					Log.e("GCM Register Error", ex.getMessage());
					return regid;
				}
			}

		}.execute(null, null, null);
	}

};
