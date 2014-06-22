package com.instasolutions.instadj;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class DownloadArtworkTask extends AsyncTask<String, Void, Bitmap>{
	
	private final WeakReference<ImageView> imageViewReference;
	
	public DownloadArtworkTask(ImageView imageView)
	{
		imageViewReference = new WeakReference<ImageView>(imageView);
	}
	
	@Override
	protected Bitmap doInBackground(String... Art_URLs) {
		try {
	        URL url = new URL(Art_URLs[0]);
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
	protected void onPostExecute(Bitmap art)
	{
		if(imageViewReference != null){
			final ImageView imageview = imageViewReference.get();
			if(imageview != null)
			{
				if(art != null)
					imageview.setImageBitmap(art);
			}
		}
	}
	
}