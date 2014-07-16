package com.instasolutions.instadj;

import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.instasolutions.instadj.util.ServiceGetHelper;
import com.instasolutions.instadj.util.ServicePostHelper;
import com.instasolutions.instadj.util.ServiceUploadHelper;

public class CurrentRoomFragment extends Fragment implements OnClickListener,
		OnSeekBarChangeListener {

	/** Called when the activity is first created. */
	private SeekBar seekbar;
	private MediaPlayer mediaplayer;
	private ImageButton btn_play;
	private ImageButton btn_next;
	private ImageButton btn_favourite;
	private ImageButton btn_like;
	private ImageButton btn_dislike;
	private ImageButton btn_closeRoom;
	private ImageButton btn_follow;
	private Handler updateHandler = new Handler();
	private Handler scoreHandler = new Handler();
	private TextView text_curTime;
	private TextView text_endTime;
	private TextView text_songName;
	private TextView text_artist;
	private TextView text_stationName;
	private TextView text_score;
	private ImageView large_art;
	private ImageView small_art;
	private double curTime = 0;
	private double endTime = 0;
	private Activity activity;
	private StationData station = null;
	private int playlistPosition = 0;
	private static Boolean initialized = false;
	private Boolean currentlyFavourited = false;
	private int currentVote = 0;
	private Boolean firstSong = false;
	private Boolean userFollowingHost = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_current_room, container,
				false);
		activity = this.getActivity();
		activity.getActionBar().setTitle(R.string.app_name);
		this.setHasOptionsMenu(true);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (initialized == false)
			initialize();
		else
			reinitialize();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.current_room, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	public void initialize() {
		playlistPosition = 0;
		currentlyFavourited = false;
		currentVote = 0;
		if (station == null) {
			StationsFragment fragment = ((ListeningRoom) activity)
					.getStationsFragment();
			FragmentManager fragmentManager = this.getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.replace(R.id.container, fragment,
					"StationsFragment");
			fragmentTransaction.commit();
			AlertDialog.Builder builder = new AlertDialog.Builder(
					this.getActivity(), android.R.style.Theme_Holo_Dialog);
			builder.setMessage("Select a station.").setTitle("No Station");
			AlertDialog dialog = builder.create();
			dialog.show();
			return;
		}

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(activity);
		Editor editPrefs = prefs.edit();

		if (station.Owner.getUserID().compareTo(prefs.getString("UserID", "0")) == 0) {
			editPrefs.putBoolean("userIsHosting", true);
		} else {
			editPrefs.putBoolean("userIsHosting", false);
		}

		editPrefs.commit();

		station.Song = station.Playlist.Songs.get(playlistPosition);
		btn_play = (ImageButton) activity.findViewById(R.id.Button_Play);
		btn_next = (ImageButton) activity.findViewById(R.id.button_next);
		btn_favourite = (ImageButton) activity
				.findViewById(R.id.room_favourite_button);
		btn_like = (ImageButton) activity.findViewById(R.id.room_like_button);
		btn_dislike = (ImageButton) activity
				.findViewById(R.id.room_dislike_button);
		btn_closeRoom = (ImageButton) activity
				.findViewById(R.id.room_close_button);
		btn_follow = (ImageButton) activity
				.findViewById(R.id.room_follow_button);
		seekbar = (SeekBar) activity.findViewById(R.id.seekBar1);
		text_curTime = (TextView) activity.findViewById(R.id.text_curTime);
		text_endTime = (TextView) activity.findViewById(R.id.text_endTime);
		text_songName = (TextView) activity.findViewById(R.id.text_song);
		text_artist = (TextView) activity.findViewById(R.id.text_artist);
		text_stationName = (TextView) activity
				.findViewById(R.id.currentroom_stationname_text);
		large_art = (ImageView) activity.findViewById(R.id.image_art);
		small_art = (ImageView) activity.findViewById(R.id.album_art);
		text_score = (TextView) activity.findViewById(R.id.room_score_text);

		btn_play.setOnClickListener(this);
		btn_favourite.setOnClickListener(this);
		btn_closeRoom.setOnClickListener(this);

		if (station != null) {
			text_stationName.setText(station.Name);
			text_songName.setText(station.Song.Title);
			text_artist.setText(station.Song.Artist);

		}
		if (prefs.getBoolean("userIsHosting", false)) {
			// At this time not allowing host to skip or change position in song
			// TODO: Determine if upload is complete before moving to next song
			seekbar.setOnSeekBarChangeListener(this);
			btn_next.setOnClickListener(this);
			btn_closeRoom.setImageResource(R.drawable.ic_action_discard);
			btn_closeRoom.setVisibility(ImageView.VISIBLE);

			btn_next.setVisibility(ImageView.VISIBLE);
			btn_like.setVisibility(ImageView.INVISIBLE);
			btn_dislike.setVisibility(ImageView.INVISIBLE);
			btn_follow.setVisibility(ImageView.INVISIBLE);
			prepareRoom();
		} else {
			btn_next.setVisibility(ImageView.INVISIBLE);

			btn_like.setOnClickListener(this);
			btn_dislike.setOnClickListener(this);
			btn_follow.setOnClickListener(this);
			btn_closeRoom.setImageResource(R.drawable.insta_leave_room);
			btn_closeRoom.setVisibility(ImageView.VISIBLE);
			btn_like.setVisibility(ImageView.VISIBLE);
			btn_dislike.setVisibility(ImageView.VISIBLE);
			btn_follow.setVisibility(ImageView.VISIBLE);
			initFollow();
			nextSong();
		}

	}

	public void reinitialize() {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(activity);

		btn_play = (ImageButton) activity.findViewById(R.id.Button_Play);
		btn_next = (ImageButton) activity.findViewById(R.id.button_next);
		btn_favourite = (ImageButton) activity
				.findViewById(R.id.room_favourite_button);
		btn_like = (ImageButton) activity.findViewById(R.id.room_like_button);
		btn_dislike = (ImageButton) activity
				.findViewById(R.id.room_dislike_button);
		btn_closeRoom = (ImageButton) activity
				.findViewById(R.id.room_close_button);
		btn_follow = (ImageButton) activity
				.findViewById(R.id.room_follow_button);
		seekbar = (SeekBar) activity.findViewById(R.id.seekBar1);
		text_curTime = (TextView) activity.findViewById(R.id.text_curTime);
		text_endTime = (TextView) activity.findViewById(R.id.text_endTime);
		text_songName = (TextView) activity.findViewById(R.id.text_song);
		text_artist = (TextView) activity.findViewById(R.id.text_artist);
		text_stationName = (TextView) activity
				.findViewById(R.id.currentroom_stationname_text);
		large_art = (ImageView) activity.findViewById(R.id.image_art);
		small_art = (ImageView) activity.findViewById(R.id.album_art);
		text_score = (TextView) activity.findViewById(R.id.room_score_text);

		btn_play.setOnClickListener(this);
		btn_favourite.setOnClickListener(this);
		btn_closeRoom.setOnClickListener(this);

		if (prefs.getBoolean("userIsHosting", false)) {
			// At this time not allowing host to skip or change position in song
			// TODO: Determine if upload is complete before moving to next song
			seekbar.setOnSeekBarChangeListener(this);
			btn_next.setOnClickListener(this);
			btn_closeRoom.setImageResource(R.drawable.ic_action_discard);
			btn_closeRoom.setVisibility(ImageView.VISIBLE);

			btn_next.setVisibility(ImageView.VISIBLE);
			btn_like.setVisibility(ImageView.INVISIBLE);
			btn_dislike.setVisibility(ImageView.INVISIBLE);
			btn_follow.setVisibility(ImageView.INVISIBLE);
		} else {
			btn_next.setVisibility(ImageView.INVISIBLE);

			btn_like.setOnClickListener(this);
			btn_dislike.setOnClickListener(this);
			btn_follow.setOnClickListener(this);
			btn_closeRoom.setImageResource(R.drawable.insta_leave_room);
			btn_closeRoom.setVisibility(ImageView.VISIBLE);
			btn_like.setVisibility(ImageView.VISIBLE);
			btn_dislike.setVisibility(ImageView.VISIBLE);
			btn_follow.setVisibility(ImageView.VISIBLE);
			initFollow();
		}

		text_endTime.setText(String.valueOf(endTime));

		if (station != null) {
			text_stationName.setText(station.Name);
			text_songName.setText(station.Song.Title);
			text_artist.setText(station.Song.Artist);

		}

		if (mediaplayer.isPlaying()) {
			btn_play.setImageResource(R.drawable.ic_action_pause);
		} else {
			btn_play.setImageResource(R.drawable.ic_action_play);
		}
		MediaMetadataRetriever media = new MediaMetadataRetriever();
		media.setDataSource(station.Song.LocalPath);
		byte[] data = media.getEmbeddedPicture();
		if (data != null) {
			Bitmap art_bitmap = BitmapFactory.decodeByteArray(data, 0,
					data.length);
			large_art.setImageBitmap(art_bitmap);
			small_art.setImageBitmap(art_bitmap);
		}

		seekbar.setMax((int) endTime);
	}

	private void initFollow() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(activity);
		ServiceGetHelper helper = new ServiceGetHelper() {
			@Override
			protected void onPostExecute(String result) {
				try {
					JSONArray followArray = new JSONArray(result);
					if (followArray.length() == 0) {
						btn_follow
								.setImageResource(R.drawable.ic_action_add_person);
						userFollowingHost = false;
					} else {
						btn_follow
								.setImageResource(R.drawable.ic_action_add_person_green);
						userFollowingHost = true;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};
		helper.executeOnExecutor(
				AsyncTask.THREAD_POOL_EXECUTOR,
				"http://instadj.amir20001.cloudbees.net/follow/getid/"
						+ prefs.getString("UserID", "UserID") + "/"
						+ station.Owner.getUserID());
	}

	public void setStation(StationData s, Activity a) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(a);
		if (station != null) {
			ServicePostHelper helper = new ServicePostHelper();
			helper.execute("http://instadj.amir20001.cloudbees.net/room/leave/"
					+ station.id + "/" + prefs.getString("UserID", "UserID"));
		}
		this.station = s;
		this.station.Playlist.populateSongs();
		if (updateHandler != null)
			updateHandler.removeCallbacks(UpdateSeekBar);
		if (mediaplayer != null)
			mediaplayer.release();

		if (text_stationName != null) {
			text_stationName.setText(station.Name);
		}
		initialized = false;
		userFollowingHost = false;

		ServicePostHelper helper = new ServicePostHelper();
		helper.execute("http://instadj.amir20001.cloudbees.net/room/join/"
				+ station.id + "/" + prefs.getString("UserID", "UserID"));
		Editor prefEdit = prefs.edit();
		prefEdit.putInt("userCurrentRoom", station.id);
		prefEdit.commit();

	}

	public void play() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(activity);
		Boolean userIsHost = prefs.getBoolean("userIsHosting", false);
		if (mediaplayer.isPlaying()) {
			mediaplayer.pause();
			btn_play.setImageResource(R.drawable.ic_action_play);
			if (userIsHost) {
				ServicePostHelper helper = new ServicePostHelper();
				helper.executeOnExecutor(
						AsyncTask.THREAD_POOL_EXECUTOR,
						"http://instadj.amir20001.cloudbees.net/room/pause/"
								+ station.id + "/"
								+ mediaplayer.getCurrentPosition());
			}
		} else {
			if (userIsHost) {
				ServicePostHelper helper = new ServicePostHelper();
				helper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
						"http://instadj.amir20001.cloudbees.net/room/play/"
								+ station.id);
				startPlayer();

			} else {
				final ProgressDialog dialog = new ProgressDialog(this.activity,
						android.R.style.Theme_Holo_Dialog);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setTitle("Syncing");
				dialog.setMessage("Please Wait...");
				dialog.setIndeterminate(true);
				ServiceGetHelper helper = new ServiceGetHelper() {
					@Override
					protected void onPostExecute(String result) {
						JSONObject jRoom;
						try {
							jRoom = new JSONObject(result);
							int currentPos = jRoom.getInt("songPosition");
							int startTime = jRoom.getInt("songPlayStartTime");
							Boolean isplaying = jRoom.getBoolean("songIsPlaying");
							dialog.dismiss();
							if(isplaying)
							{
								//Note could be a timezone issue here
								long positionDifference = System.currentTimeMillis()-startTime;
								mediaplayer.seekTo((int)(currentPos + positionDifference));
								startPlayer();
							}
							else
							{
								Toast.makeText(activity, "Room paused by DJ.", Toast.LENGTH_SHORT).show();
							}

						} catch (JSONException e) {

							e.printStackTrace();
						}

					}

				};
				helper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
						"http://instadj.amir20001.cloudbees.net/room/get/"
								+ station.id);
			}
		}
	}

	private void startPlayer() {
		mediaplayer.start();
		btn_play.setImageResource(R.drawable.ic_action_pause);
		curTime = mediaplayer.getCurrentPosition();
		endTime = mediaplayer.getDuration();

		text_endTime.setText(String.format(
				"%d:%02d",
				TimeUnit.MILLISECONDS.toMinutes((long) endTime),
				TimeUnit.MILLISECONDS.toSeconds((long) endTime)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes((long) endTime))));
		text_curTime.setText(String.format(
				"%d:%02d",
				TimeUnit.MILLISECONDS.toMinutes((long) curTime),
				TimeUnit.MILLISECONDS.toSeconds((long) curTime)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes((long) curTime))));
		seekbar.setProgress((int) curTime);
		updateHandler.postDelayed(UpdateSeekBar, 100);
	}

	private Runnable UpdateSeekBar = new Runnable() {
		public void run() {
			if (mediaplayer != null && mediaplayer.isPlaying()) {
				curTime = mediaplayer.getCurrentPosition();
				seekbar.setProgress((int) curTime);
				text_curTime.setText(String.format(
						"%d:%02d",
						TimeUnit.MILLISECONDS.toMinutes((long) curTime),
						TimeUnit.MILLISECONDS.toSeconds((long) curTime)
								- TimeUnit.MINUTES
										.toSeconds(TimeUnit.MILLISECONDS
												.toMinutes((long) curTime))));
			}
			// Song Complete
			if (curTime >= endTime) {
				nextSong();
			} else {
				updateHandler.postDelayed(this, 100);
			}
		}
	};

	private void nextSong() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(activity);
		if (updateHandler != null)
			updateHandler.removeCallbacks(UpdateSeekBar);
		if (mediaplayer != null)
			mediaplayer.release();
		// Add favorited song to database
		if (currentlyFavourited) {
			ServicePostHelper helper = new ServicePostHelper();
			helper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
					"http://instadj.amir20001.cloudbees.net/favourite/insert/"
							+ prefs.getString("UserID", "0") + "/"
							+ station.Song.id);
		}
		if (currentVote != 0) {
			ServicePostHelper helper = new ServicePostHelper();
			helper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
					"http://instadj.amir20001.cloudbees.net/song/vote/"
							+ station.Song.id + "/" + currentVote);
		}
		// Remove previous song from storage
		if (prefs.getBoolean("userIsHosting", false) && !firstSong) {
			ServicePostHelper helper = new ServicePostHelper();
			helper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
					"http://instadj.amir20001.cloudbees.net/song/delete/"
							+ station.Song.id);
		}
		if (!initialized) {
			initialized = true;
		} else {
			((RightDrawerFragment) getFragmentManager().findFragmentById(
					R.id.navigation_drawer_right))
					.updateSongHistory(station.Song);
		}

		if (prefs.getBoolean("userIsHosting", false)) {
			if (!firstSong) {
				ServicePostHelper helper = new ServicePostHelper();
				helper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
						"http://instadj.amir20001.cloudbees.net/room/updatecurrentsong/"
								+ station.id + "/" + station.Song.id);
			}
			if (playlistPosition >= station.Playlist.Songs.size()) {
				playlistPosition = 0;
			}
			station.Song = station.Playlist.Songs.get(playlistPosition);
			// Upload next song
			ServiceUploadHelper upload = new ServiceUploadHelper();
			upload.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
					station.Playlist.Songs
							.get(playlistPosition + 1 >= station.Playlist.Songs
									.size() ? 0 : playlistPosition + 1));

			mediaplayer = MediaPlayer.create(activity,
					new Uri.Builder().path(station.Song.LocalPath).build());
			// Async task currently takes some time to fire update the art to
			// blank until it does
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
			seekbar.setMax((int) endTime);
			firstSong = false;

			currentlyFavourited = false;
			currentVote = 0;
			btn_like.setImageResource(R.drawable.ic_action_good);
			btn_dislike.setImageResource(R.drawable.ic_action_bad);
			btn_favourite.setImageResource(R.drawable.ic_action_favorite);
			play();
			playlistPosition++;
		} else {
			final ProgressDialog dialog = new ProgressDialog(this.activity,
					android.R.style.Theme_Holo_Dialog);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setTitle("Getting Next Song");
			dialog.setMessage("Please Wait...");
			dialog.setIndeterminate(true);
			ServiceGetHelper helper = new ServiceGetHelper() {
				@Override
				protected void onPostExecute(String result) {
					try {
						JSONObject jStation = new JSONObject(result);
						JSONObject jSong = jStation.getJSONObject("song");
						SongData song = new SongData(jSong.getInt("id"),
								jSong.getString("title"),
								jSong.getString("artist"),
								jSong.getString("album"),
								jSong.getString("duration"),
								jSong.getString("songUri"),
								jSong.getString("artUrl"),
								jSong.getString("songUrl"));
						station.Song = song;
					} catch (JSONException e) {
						e.printStackTrace();
					}
					dialog.dismiss();
					mediaplayer = MediaPlayer.create(activity,
							new Uri.Builder().path(station.Song.Song_URL)
									.build());
					// Async task currently takes some time to fire update the
					// art to blank until it does
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
					seekbar.setMax((int) endTime);
					firstSong = false;

					currentlyFavourited = false;
					currentVote = 0;
					btn_like.setImageResource(R.drawable.ic_action_good);
					btn_dislike.setImageResource(R.drawable.ic_action_bad);
					btn_favourite
							.setImageResource(R.drawable.ic_action_favorite);
					play();
				}
			};
			helper.execute("http://instadj.amir20001.cloudbees.net/room/get/"
					+ station.id);

		}

	}

	private void prepareRoom() {
		final ProgressDialog dialog = new ProgressDialog(this.activity,
				android.R.style.Theme_Holo_Dialog);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setTitle("Preparing Room");
		dialog.setMessage("Please Wait...");
		dialog.setIndeterminate(true);
		final CurrentRoomFragment room = this;
		dialog.show();
		ServiceUploadHelper upload = new ServiceUploadHelper() {
			@Override
			protected void onPostExecute(Integer i) {
				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(activity);
				dialog.dismiss();
				try {
					JSONObject jstation = new JSONObject();
					jstation.put("name", station.Name);
					jstation.put("ownerUserId", prefs.getString("UserID", "0"));
					jstation.put("playlistId", station.Playlist.id);
					jstation.put("listenerCount", station.ListenerCount);
					jstation.put("currentSongId", station.Song.id);

					ServicePostHelper post = new ServicePostHelper();
					post.executeOnExecutor(
							AsyncTask.THREAD_POOL_EXECUTOR,
							"http://instadj.amir20001.cloudbees.net/room/insert",
							jstation.toString());
					String jString = post.get();
					jstation = new JSONObject(jString);
					station.id = jstation.getInt("id");
					ServicePostHelper helper = new ServicePostHelper();
					helper.executeOnExecutor(
							AsyncTask.THREAD_POOL_EXECUTOR,
							"http://instadj.amir20001.cloudbees.net/room/join/"
									+ station.id + "/"
									+ prefs.getString("UserID", "UserID"));
					Editor prefEdit = prefs.edit();
					prefEdit.putInt("userCurrentRoom", station.id);
					prefEdit.commit();

				} catch (Exception e) {
					Log.e("instaDJ", "JSONException", e);
				}
				room.nextSong();
			}
		};
		upload.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, station.Song);
		firstSong = true;

	}

	private void updateLikes(Boolean like) {
		if (like) {
			if (currentVote == 1) {
				currentVote = 0;
				btn_like.setImageResource(R.drawable.ic_action_good);
			} else {
				currentVote = 1;
				btn_like.setImageResource(R.drawable.ic_action_good_green);
				btn_dislike.setImageResource(R.drawable.ic_action_bad);
			}
		} else {
			if (currentVote == -1) {
				currentVote = 0;
				btn_dislike.setImageResource(R.drawable.ic_action_bad);
			} else {
				currentVote = -1;
				btn_like.setImageResource(R.drawable.ic_action_good);
				btn_dislike.setImageResource(R.drawable.ic_action_bad_red);
			}

		}
	}

	private void updateFavourites() {
		if (currentlyFavourited) {
			btn_favourite.setImageResource(R.drawable.ic_action_favorite);
			currentlyFavourited = false;
		} else {
			btn_favourite.setImageResource(R.drawable.ic_action_favorite_red);
			currentlyFavourited = true;
		}
	}

	public void closeRoom() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(activity);
		if (updateHandler != null)
			updateHandler.removeCallbacks(UpdateSeekBar);
		if (mediaplayer != null)
			mediaplayer.release();
		if (prefs.getBoolean("userIsHosting", false)) {
			ServicePostHelper helper = new ServicePostHelper();
			helper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
					"http://instadj.amir20001.cloudbees.net/room/delete/"
							+ station.id);
		} else {
			ServicePostHelper helper = new ServicePostHelper();
			helper.executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR,
					"http://instadj.amir20001.cloudbees.net/room/leave/"
							+ station.id + "/"
							+ prefs.getString("UserID", "UserID"));
		}
		station = null;
		initialized = false;
		Editor prefEditor = prefs.edit();
		prefEditor.putBoolean("userIsHosting", false);
		prefEditor.commit();
		// Go to default fragment
		((ListeningRoom) activity).onNavigationDrawerItemSelected(-1);
	}

	public void forceQuitRoom() {
		// AlertDialog.Builder builder = new
		// AlertDialog.Builder(this.getActivity(),
		// android.R.style.Theme_Holo_Dialog);
		// builder.setMessage("The host has closed the room.").setTitle("Room Closed");
		// builder.show();
		Toast.makeText(activity, "Room closed.", Toast.LENGTH_SHORT).show();
		;
		if (updateHandler != null)
			updateHandler.removeCallbacks(UpdateSeekBar);
		if (mediaplayer != null)
			mediaplayer.release();
		station = null;
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(activity);
		Editor prefEdit = prefs.edit();
		prefEdit.putInt("userCurrentRoom", -1);
		prefEdit.commit();
		initialized = false;
		// Go to default fragment
		((ListeningRoom) activity).onNavigationDrawerItemSelected(-1);
	}

	public void displayScore(final int score) {

		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				text_score.setText(String.valueOf(score));
				text_score.setVisibility(ImageView.VISIBLE);
			}

		});
		scoreHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						text_score.setVisibility(ImageView.INVISIBLE);

					}

				});

			}
		}, 5000);

	}

	private void updateFollowing() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(activity);
		String userID = prefs.getString("UserID", "UserID");
		if (!userFollowingHost) {
			userFollowingHost = true;
			btn_follow.setImageResource(R.drawable.ic_action_add_person_green);
			ServicePostHelper helper = new ServicePostHelper();
			helper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
					"http://instadj.amir20001.cloudbees.net/follow/insert/"
							+ userID + "/" + station.Owner.getUserID());
		} else {
			userFollowingHost = false;
			btn_follow.setImageResource(R.drawable.ic_action_add_person);
			ServicePostHelper helper = new ServicePostHelper();
			helper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
					"http://instadj.amir20001.cloudbees.net/follow/delete/"
							+ userID + "/" + station.Owner.getUserID());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
		case R.id.room_close_button:
			closeRoom();
			break;
		case R.id.room_follow_button:
			updateFollowing();
		default:
			break;
		}

	}

	@Override
	public void onProgressChanged(SeekBar s, int i, boolean userInput) {
		if (userInput) {
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
