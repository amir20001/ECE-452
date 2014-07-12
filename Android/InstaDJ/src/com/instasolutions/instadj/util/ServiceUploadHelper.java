package com.instasolutions.instadj.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

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
  	    String serverURL = "http://instadj.amir20001.cloudbees.net/song/upload2/" + song.id;
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
                  
                   // open a URL connection to the Servlet
                 FileInputStream fileInputStream = new FileInputStream(sourceFile);
                 URL url = new URL(serverURL);
                   
                 // Open a HTTP  connection to  the URL
                 conn = (HttpURLConnection) url.openConnection(); 
                 conn.setDoInput(true); // Allow Inputs
                 conn.setDoOutput(true); // Allow Outputs
                 conn.setUseCaches(false); // Don't use a Cached Copy
                 conn.setRequestMethod("POST");
                 conn.setRequestProperty("Connection", "Keep-Alive");
                 conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                 conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                 conn.setRequestProperty("file", fileName); 
                   
                 dos = new DataOutputStream(conn.getOutputStream());
         
                 dos.writeBytes(twoHyphens + boundary + lineEnd); 
                   dos.writeBytes("Content-Disposition: form-data; name=file;filename="+ fileName + ""+ lineEnd);
                 dos.writeBytes(lineEnd);
         
                 // create a buffer of  maximum size
                 bytesAvailable = fileInputStream.available(); 
        
                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
                 buffer = new byte[bufferSize];
         
                 // read file and write it into form...
                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                     
                 while (bytesRead > 0) {
                       
                   dos.write(buffer, 0, bufferSize);
                   bytesAvailable = fileInputStream.available();
                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
                   bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
                     
                   }
         
                 // send multipart form data necesssary after file data...
                 dos.writeBytes(lineEnd);
                 dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
         
                 // Responses from the server (code and message)
                 int serverResponseCode = conn.getResponseCode();
                 String serverResponseMessage = conn.getResponseMessage();
                    
                 Log.i("uploadFile", "HTTP Response is : "
                         + serverResponseMessage + ": " + serverResponseCode);         
                  
                   
                 //close the streams //
                 fileInputStream.close();
                 dos.flush();
                 dos.close();
                 return serverResponseCode;
                    
            } catch (Exception e) {
                  
                e.printStackTrace();
                  
                Log.e("Upload file to server Exception", "Exception : "
                                                  + e.getMessage(), e);  
                return -1;
            } 
        }

	}
	

}
