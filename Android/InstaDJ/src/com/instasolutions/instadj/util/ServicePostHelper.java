package com.instasolutions.instadj.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Handler.Callback;
import android.util.Log;

public class ServicePostHelper extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... params) {
		String returnString = "";
		if(params.length < 1 || params.length > 2)
		{
			return returnString;
		}
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = null;
			if(params.length == 1 || params.length == 2)
				 httpPost = new HttpPost(params[0]);
			if(params.length == 2)
			{
				StringEntity se = new StringEntity(params[1]);
				httpPost.setEntity(se);
			}
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			System.out.println("executing request " + httpPost.getRequestLine());
			HttpResponse httpResponse = httpclient.execute(httpPost);
			BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			if(br != null){
				returnString = br.readLine();
			}
			System.out.println("server response: " + returnString + "(" + httpPost.getRequestLine() +")");
			return returnString;
		} catch (Exception e) {
			Log.e("instadj", "exception", e);
			return returnString;
		}

	}
	

}
