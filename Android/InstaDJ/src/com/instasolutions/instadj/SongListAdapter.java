package com.instasolutions.instadj;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.instasolutions.instadj.util.ServicePostHelper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton;

public class SongListAdapter extends BaseAdapter implements
		CompoundButton.OnCheckedChangeListener, Filterable {

	private Activity activity;
	private SparseArray<SongData> songs = new SparseArray<SongData>();
	private SparseArray<SongData> songsFiltered = new SparseArray<SongData>();
	private SparseBooleanArray checkBoxArray = new SparseBooleanArray();
	private static LayoutInflater inflater = null;
	private Boolean UseSelect = false;
	private Boolean UseButtons = false;
	private ListView lv = null;
	public static final int FILTERBYTITLE = 0;
	public static final int FILTERBYARTIST = 1;
	public static final int FILTERBYALBUM = 2;

	public SongListAdapter(Activity a, SparseArray<SongData> d) {
		activity = a;
		songs = d;
		songsFiltered = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public SongListAdapter(Activity a, SparseArray<SongData> d,
			Boolean useSelect) {
		activity = a;
		songs = d;
		songsFiltered = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		UseSelect = useSelect;
	}

	public SparseBooleanArray getCheckBoxArray() {
		return checkBoxArray;
	}

	@Override
	public int getCount() {

		return songsFiltered.size();
	}

	@Override
	public Object getItem(int pos) {
		return songs.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View view, ViewGroup viewgroup) {
		View v = view;
		if (view == null) {
			if (UseSelect) {
				v = inflater.inflate(R.layout.list_row_songs_select, null);
			} else
				v = inflater.inflate(R.layout.list_row_songs, null);
		}

		TextView title = (TextView) v.findViewById(R.id.song_title);
		TextView artist = (TextView) v.findViewById(R.id.song_artist);
		TextView album = (TextView) v.findViewById(R.id.song_album);
		TextView duration = (TextView) v.findViewById(R.id.song_duration);
		ImageView art = (ImageView) v.findViewById(R.id.list_album_art);

		final SongData song = songsFiltered.get(pos);

		title.setText(song.Title);
		artist.setText(song.Artist);
		album.setText(song.Album);
		duration.setText(song.Duration);

		if (UseSelect) {
			TextView SongPath = (TextView) v
					.findViewById(R.id.song_contentpath);
			CheckBox checkbox = (CheckBox) v.findViewById(R.id.songs_checkbox);
			int id = Resources.getSystem().getIdentifier("btn_check_holo_dark",
					"drawable", "android");
			checkbox.setButtonDrawable(id);
			Boolean checked = checkBoxArray.get(song.id);
			SongPath.setText(song.LocalPath);
			checkbox.setTag(song.id);
			checkbox.setOnCheckedChangeListener(null);
			if (checked != null) {
				checkbox.setChecked(checked);
				;
			} else {
				checkbox.setChecked(false);
				checkBoxArray.append(song.id, false);
			}

			checkbox.setOnCheckedChangeListener(this);

		} else {
			ImageButton deleteButton = (ImageButton) v
					.findViewById(R.id.song_delete_button);
			if (UseButtons) {
				deleteButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						SharedPreferences prefs = PreferenceManager
								.getDefaultSharedPreferences(activity);
						ServicePostHelper helper = new ServicePostHelper();
						helper.executeOnExecutor(
								AsyncTask.THREAD_POOL_EXECUTOR,
								"http://instadj.amir20001.cloudbees.net/favourite/delete/"
										+ prefs.getString("UserID", "UserID")
										+ "/" + song.id);
						try {
							helper.get();
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}

						FavoritesFragment currentFragment = ((ListeningRoom) activity)
								.getFavoritesFragment();
						FragmentActivity fragActivity = (FragmentActivity) activity;
						FragmentTransaction fragTransaction = fragActivity
								.getSupportFragmentManager().beginTransaction();
						fragTransaction.detach(currentFragment);
						fragTransaction.attach(currentFragment);
						fragTransaction.commit();
					}
				});
			} else {
				deleteButton.setVisibility(ImageView.INVISIBLE);
			}
		}

		art.setImageBitmap(BitmapFactory.decodeResource(
				activity.getResources(), R.drawable.blankart));

		if (song.Art_URL != null && song.Art_URL != "null") {
			final GettArtworkTask task = new GettArtworkTask(art);
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, song.Art_URL);
		} else if (song.LocalPath != null && song.LocalPath != "null") {
			final GettArtworkTask task = new GettArtworkTask(art);
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
					song.LocalPath);
		} else if (song.Art_Bitmap != null) {
			art.setImageBitmap(song.Art_Bitmap);
		}

		return v;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView.isChecked())
			checkBoxArray.put((Integer) buttonView.getTag(), true);
		else
			checkBoxArray.put((Integer) buttonView.getTag(), false);

	}

	public SparseArray<SongData> getArray() {
		return songs;
	}

	public void setArray(SparseArray<SongData> sArray) {
		songs = sArray;
	}

	public void setButtonsEnabled(boolean buttons) {
		this.UseButtons = buttons;
	}

	public Filter getFilter() {
		return new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence charSequence) {
				FilterResults results = new FilterResults();

				if (charSequence == null || charSequence.length() == 0) {
					results.values = songs;
					results.count = songs.size();
				} else {
					SparseArray<SongData> filteredSongs = new SparseArray<SongData>();
					int c = 0;
					for (int i = 0; i < songs.size(); i++) {

						SongData song = songs.get(i);
						if (song.Title.contains(charSequence)) {
							filteredSongs.append(c, song);
							c++;
						}
					}

					results.values = filteredSongs;
					results.count = filteredSongs.size();
				}

				return results;
			}

			@Override
			protected void publishResults(CharSequence charSequence,
					FilterResults filterResults) {
				songsFiltered = (SparseArray<SongData>) filterResults.values;
				notifyDataSetChanged();
			}
		};
	}

	public Filter getFilter(final int filterBy) {
		return new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence charSequence) {
				FilterResults results = new FilterResults();

				if (charSequence == null || charSequence.length() == 0) {
					results.values = songs;
					results.count = songs.size();
				} else {
					SparseArray<SongData> filteredSongs = new SparseArray<SongData>();
					int c = 0;
					for (int i = 0; i < songs.size(); i++) {

						SongData song = songs.get(i);
						String compareThis = "";
						switch (filterBy) {
						case FILTERBYTITLE:
							compareThis = song.Title;
							break;
						case FILTERBYARTIST:
							compareThis = song.Artist;
							break;
						case FILTERBYALBUM:
							compareThis = song.Album;
							break;
						default:
							compareThis = song.Title;
							break;

						}
						if (compareThis.contains(charSequence)) {
							filteredSongs.append(c, song);
							c++;
						}
					}

					results.values = filteredSongs;
					results.count = filteredSongs.size();
				}

				return results;
			}

			@Override
			protected void publishResults(CharSequence charSequence,
					FilterResults filterResults) {
				songsFiltered = (SparseArray<SongData>) filterResults.values;
				notifyDataSetChanged();
			}
		};
	}
}
