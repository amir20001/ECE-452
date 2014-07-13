package com.instasolutions.instadj.util;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class ServiceGetHelper extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... params) {
		if (params.length <= 0)
			return null;
		try {
			InputStream inputStream = null;
			String result = "";
			try {

				// create HttpClient
				HttpClient httpclient = new DefaultHttpClient();

				// make get request to the given URL
				HttpGet httpget = new HttpGet(params[0]);

				HttpResponse httpResponse = httpclient.execute(httpget);
				inputStream = httpResponse.getEntity().getContent();
				if (inputStream != null) {
					result = ServiceUtil.convertInputStreamToString(inputStream);

				}
				return result;
			} catch (Exception e) {
				Log.e("instadj", "exception", e);
				return result;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
}
