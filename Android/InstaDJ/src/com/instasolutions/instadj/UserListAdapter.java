package com.instasolutions.instadj;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import com.instasolutions.instadj.util.DownloadPictureTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
		UserName.setText(user.getFirstName() + " " + user.getLastName().charAt(0) + ".");
		Picture.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.com_facebook_profile_picture_blank_square));
		
		final DownloadPictureTask task = new DownloadPictureTask(Picture);
		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 
				"https://graph.facebook.com/" + user.getUserID() + "/picture?type=square");
		
		return v;
	}
	
	public SparseArray<UserData> getArray()
	{
		return users;
	}
	
	public void setArray(SparseArray<UserData> uArray)
	{
		users = uArray;
	}

}
