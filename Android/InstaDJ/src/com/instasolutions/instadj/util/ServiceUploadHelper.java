package com.instasolutions.instadj.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import com.instasolutions.instadj.SongData;

import android.os.AsyncTask;
import android.os.Handler.Callback;
import android.util.Log;
import android.widget.Toast;

public class ServiceUploadHelper extends AsyncTask<SongData, Void, Integer> {

	@Override
	protected Integer doInBackground(SongData... songs) {
		 
		SongData song = songs[0];
		final String fileName = song.LocalPath;
  	    String serverURL = "http://instadj.amir20001.cloudbees.net/song/upload/" + song.id;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;  
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024; 
        File sourceFile = new File(fileName); 
          
        if (!sourceFile.isFile()) {
             
             Log.e("uploadFile", "Source File not exist :"
                                 + fileName);
             return -1;
          
        }
        else
        {
        	try {
        	 HttpClient httpclient = new DefaultHttpClient();
             httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
             HttpPost httppost = new HttpPost(serverURL);
             File file = new File(fileName);
             MultipartEntity  mpEntity = new MultipartEntity();
             ContentBody cbFile = new FileBody(file);
             mpEntity.addPart("file", cbFile); 
             httppost.setEntity(mpEntity);
             System.out.println("executing request " + httppost.getRequestLine());
             HttpResponse response;
			
			 response = httpclient.execute(httppost);
             HttpEntity resEntity = response.getEntity();
             return 1;
        	} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return 0;

	}
	

}
