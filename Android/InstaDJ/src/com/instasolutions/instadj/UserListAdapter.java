package com.instasolutions.instadj;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class UserListAdapter extends BaseAdapter{

	private Activity activity;
	private SparseArray<UserData> users = new SparseArray<UserData>();
	private static LayoutInflater inflater = null;
	
	public UserListAdapter(Activity a, SparseArray<UserData> d)
	{
		activity = a;
		users = d;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		
		return users.size();
	}

	@Override
	public Object getItem(int pos) {
		return users.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View view, ViewGroup viewgroup) {
		View v = view;
		if(view==null)
			v = inflater.inflate(R.layout.list_row_users, null);
		
		TextView UserName = (TextView)v.findViewById(R.id.user_name);
        TextView Score = (TextView)v.findViewById(R.id.text_score);
		ImageView Picture = (ImageView)v.findViewById(R.id.list_user_picture);

        UserData user = users.get(pos);

        Score.setText("Score: " + user.getScore());
		UserName.setText(user.getFirstName() + " " + user.getLastName());
		Picture.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.com_facebook_profile_picture_blank_square));
		
		final DownloadPictureTask task = new DownloadPictureTask(Picture);
		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
				"https://graph.facebook.com/" + user.getUserID() + "/picture?type=square");
		
		return v;
	}
	
	
	private class DownloadPictureTask extends AsyncTask<String, Void, Bitmap>{
		
		private final WeakReference<ImageView> imageViewReference;
		
		public DownloadPictureTask(ImageView imageView)
		{
			imageViewReference = new WeakReference<ImageView>(imageView);
		}
		
		@Override
		protected Bitmap doInBackground(String... Picture_URLs) {

            try {
		        URL url = new URL(Picture_URLs[0]);
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setDoInput(true);
		        connection.connect();
		        InputStream input = connection.getInputStream();
		        return BitmapFactory.decodeStream(input);
		        
		    } catch (IOException e) {
		        e.printStackTrace();
		        return null;
		    }
		}
		
		@Override
		protected void onPostExecute(Bitmap picture)
		{
			if(imageViewReference != null){
				final ImageView imageview = imageViewReference.get();
				if(imageview != null)
				{
					if(picture != null)
						imageview.setImageBitmap(picture);
				}
			}
		}
		
	}

}
