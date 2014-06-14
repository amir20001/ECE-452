package com.instasolutions.instadj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

class PostTest extends AsyncTask<String, Void, String> {

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	@Override
	protected String doInBackground(String... Art_URLs) {
//	 use something like this to call
//		final  PostTest post= new PostTest();
//		post.execute("http://192.168.1.137:8080/1.0.0-BUILD-SNAPSHOT/user/addfavourite")
		try {
			InputStream inputStream = null;
			String result = "";
			try {

				// 1. create HttpClient
				HttpClient httpclient = new DefaultHttpClient();

				// 2. make POST request to the given URL
				HttpPost httpPost = new HttpPost(Art_URLs[0]);

				String json = "";

				// 3. build jsonObject
				JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("songName", "songName");
				jsonObject.accumulate("artist", "artiest");

				// 4. convert JSONObject to JSON to String
				json = jsonObject.toString();

				// ** Alternative way to convert Person object to JSON string
				// usin
				// Jackson Lib
				// ObjectMapper mapper = new ObjectMapper();
				// json = mapper.writeValueAsString(person);

				// 5. set json to StringEntity
				StringEntity se = new StringEntity(json);

				// 6. set httpPost Entity
				httpPost.setEntity(se);

				// 7. Set some headers to inform server about the type of the
				// content
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");
				Log.w("test", "about to send");
				// 8. Execute POST request to the given URL
				HttpResponse httpResponse = httpclient.execute(httpPost);
				Log.w("test", "data sent");
				// 9. receive response as inputStream
				inputStream = httpResponse.getEntity().getContent();

				// 10. convert inputstream to string
				if (inputStream != null)
					result = convertInputStreamToString(inputStream);
				else
					result = "Did not work!";

			} catch (Exception e) {
				Log.e("MYAPP", "exception", e);

			}

			// 11. return result
			return "";

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
