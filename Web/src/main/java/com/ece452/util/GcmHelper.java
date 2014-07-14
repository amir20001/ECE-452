package com.ece452.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.codehaus.jackson.map.ObjectMapper;

public class GcmHelper {
	//public static final String serverKey = "AIzaSyBp0BHGUodig8fjXlJhZhr0uJFVnX7IZus";
	public static final String serverKey = "AIzaSyAqX7jbGycSTtHXBHcRdHaCitX5yB9_iQc";
	public static void post(Content content) {

		try {

			URL url = new URL("https://android.googleapis.com/gcm/send");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "key=" + serverKey);
			conn.setDoOutput(true);
			
			ObjectMapper mapper = new ObjectMapper();
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			
			mapper.writeValue(wr, content);

			System.out.println("content json");
			System.out.println(mapper.writeValueAsString(content));
			// 5.4 Send the request
			wr.flush();

			// 5.5 close
			wr.close();

			// 6. Get the response
			int responseCode = conn.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println(response.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
