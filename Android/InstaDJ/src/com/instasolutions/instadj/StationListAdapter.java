package com.instasolutions.instadj;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class StationListAdapter extends BaseAdapter implements Filterable {

	private Activity activity;
	private SparseArray<StationData> stations = new SparseArray<StationData>();
	private static LayoutInflater inflater = null;
    private SparseArray<StationData> stationsFiltered = new SparseArray<StationData>();

    public static final int FILTERBYTITLE = 0;
    public static final int FILTERBYDJ = 1;
    public static final int FILTERBYSTATION = 2;
    public static final int FILTERBYARTIST = 3;
	
	public StationListAdapter(Activity a, SparseArray<StationData> d)
	{
		activity = a;
		stations = d;
        stationsFiltered = d;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		
		return stations.size();
	}

	@Override
	public Object getItem(int pos) {
		return stations.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View view, ViewGroup viewgroup) {
		View v = view;
		if(view==null)
			v = inflater.inflate(R.layout.list_row_stations, null);
		
		TextView StationName = (TextView)v.findViewById(R.id.station_name);
		TextView StationOwner = (TextView)v.findViewById(R.id.station_owner);
		TextView StationPlaylist = (TextView)v.findViewById(R.id.station_playlist_name);
		TextView StationPlaylistGenre = (TextView)v.findViewById(R.id.station_playlist_genre);
		TextView StationSongName = (TextView)v.findViewById(R.id.station_song_name);
		TextView StationSongArtist = (TextView)v.findViewById(R.id.station_song_artist);
		TextView StationListeningCount = (TextView)v.findViewById(R.id.station_listener_count);
		ImageView StationSongArt = (ImageView)v.findViewById(R.id.station_song_art);
			
		final StationData station = stationsFiltered.get(pos);

        StationName.setText(station.Name);
        if (station.Owner != null) {
            StationOwner.setText(station.Owner.getFirstName() + " " + station.Owner.getLastName().charAt(0) + ".");
        }
        if (station.Playlist != null) {
            StationPlaylist.setText(station.Playlist.Name);
            StationPlaylistGenre.setText(station.Playlist.Genre);
        }

        StationListeningCount.setText(String.valueOf(station.ListenerCount));

        if (station.Song != null) {
            StationSongName.setText(station.Song.Title);
            StationSongArtist.setText(station.Song.Artist);
            final GettArtworkTask task = new GettArtworkTask(StationSongArt);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                    station.Song.Art_URL);
        }

		return v;
	}

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();

                if (charSequence == null || charSequence.length() == 0) {
                    results.values = stations;
                    results.count = stations.size();
                } else {
                    SparseArray<StationData> filteredStations = new SparseArray<StationData>();
                    int c = 0;
                    for (int i = 0; i < stations.size(); i++) {

                        StationData station = stations.get(i);
                        if (station.Song.Title.contains(charSequence)) {
                            filteredStations.append(c, station);
                            c++;
                        }
                    }

                    results.values = filteredStations;
                    results.count = filteredStations.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence,
                                          FilterResults filterResults) {
                stationsFiltered = (SparseArray<StationData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public Filter getFilter(final int filterBy) {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                FilterResults results = new FilterResults();

                if(charSequence == null || charSequence.length() == 0)
                {
                    results.values = stations;
                    results.count = stations.size();
                }
                else
                {
                    SparseArray<StationData> filteredStations = new  SparseArray<StationData>();
                    int c = 0;
                    for(int i = 0; i < stations.size(); i++)
                    {

                        StationData station = stations.get(i);
                        String compareThis = "";
                        switch(filterBy)
                        {
                            case FILTERBYTITLE:
                                compareThis = station.Song.Title;
                                break;
                            case FILTERBYDJ:
                                compareThis = station.Owner.getFirstName() + " " + station.Owner.getLastName();
                                break;
                            case FILTERBYSTATION:
                                compareThis = station.Name;
                                break;
                            case FILTERBYARTIST:
                                compareThis = station.Song.Artist;
                            default:
                                compareThis = station.Song.Title;
                                break;

                        }
                        if(compareThis.contains(charSequence))
                        {
                            filteredStations.append(c, station);
                            c++;
                        }
                    }

                    results.values = filteredStations;
                    results.count = filteredStations.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                stationsFiltered = (SparseArray<StationData>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
