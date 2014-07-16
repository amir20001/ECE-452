package com.instasolutions.instadj;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.R;
import android.app.AlertDialog;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private String TAG = "GcmIntentService";
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) { 
        	if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                String action = extras.getString("action");
                if(action == null)
                	return;
                CurrentRoomFragment currentRoom = ListeningRoom.mThis.getCurrentRoomFragment();
                if(action.compareTo("kick") == 0)
                {
                	//Kick user out of room
                	currentRoom.forceQuitRoom();
             	
                }
                else if(action.compareTo("score") == 0)
                {
                	//Push score
                	JSONObject jSong;
					try {
						jSong = new JSONObject(extras.getString("song"));
	                	SongData song = new SongData(jSong.getInt("id"),
								jSong.getString("title"), 
								jSong.getString("artist"),
								jSong.getString("album"),
								jSong.getString("duration"),
								jSong.getString("songUri"),
								jSong.getString("artUrl"),
								jSong.getString("songUrl"),
								jSong.getInt("netScore"));
	                	currentRoom.displayScore(song);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

                	
                }
                else if(action.compareTo("roompause") == 0)
                {
                	currentRoom.play();
                }
                else if(action.compareTo("roomplay") == 0)
                {
                	currentRoom.play();
                }
                
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

}