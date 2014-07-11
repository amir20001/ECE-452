package com.instasolutions.instadj;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.widget.ImageView;

public class GettArtworkTask extends AsyncTask<String, Void, Bitmap>{
	
	private final WeakReference<ImageView> imageViewReference;
	
	public GettArtworkTask(ImageView imageView)
	{
		imageViewReference = new WeakReference<ImageView>(imageView);
	}
	
	@Override
	protected Bitmap doInBackground(String... Art_URLs) {
		
		if(Art_URLs == null || Art_URLs.length == 0|| Art_URLs[0].equals("") || Art_URLs[0] == null || Art_URLs[0] == "null")
		{
			return null;
		}
		
		try{
			if(Art_URLs[0].contains("http"))
			{
			        URL url = new URL(Art_URLs[0]);
			        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			        connection.setDoInput(true);
			        connection.connect();
			        InputStream input = connection.getInputStream();
			        return BitmapFactory.decodeStream(input);
			       
			}
			else
			{
	    	   MediaMetadataRetriever media = new MediaMetadataRetriever();
	           media.setDataSource(Art_URLs[0]);
	           byte[] data = media.getEmbeddedPicture();
	           if(data != null)
	           {
	           		return BitmapFactory.decodeByteArray(data, 0, data.length);
	           }
	           else
	           {
	        	   return null;
	           }
			}
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