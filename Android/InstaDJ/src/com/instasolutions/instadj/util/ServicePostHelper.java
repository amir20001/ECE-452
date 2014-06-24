package com.instasolutions.instadj.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class ServicePostHelper extends AsyncTask<String, Void, Void> {

	@Override
	protected Void doInBackground(String... params) {
		if (params.length < 2)
			return null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(params[0]);
			StringEntity se = new StringEntity(params[1]);
			httpPost.setEntity(se);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			HttpResponse httpResponse = httpclient.execute(httpPost);
			httpResponse.getEntity().getContent();
			// we can do stuff with the response later on if we want
			return null;
		} catch (Exception e) {
			Log.e("instadj", "exception", e);
			return null;
		}

	}

}
