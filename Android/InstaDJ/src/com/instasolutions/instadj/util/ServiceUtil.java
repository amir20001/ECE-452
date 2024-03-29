package com.instasolutions.instadj.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServiceUtil {

	public static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		InputStreamReader iStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(iStreamReader);
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

}
