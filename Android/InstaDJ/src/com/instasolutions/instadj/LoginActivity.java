package com.instasolutions.instadj;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.PlusClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class LoginActivity extends Activity implements OnClickListener,
	ConnectionCallbacks, OnConnectionFailedListener{

	//Facebook variables
	private static final String fb_TAG = "LoginActivity";
	private UiLifecycleHelper fb_uiHelper;
	
	private Session.StatusCallback fb_callback = new Session.StatusCallback(){
		@Override
		public void call(Session session, SessionState state, Exception exception){
			fb_onSessionStateChange(session, state, exception);
		}
	};
	
	
	//Google Variables
	private boolean g_SignInClicked;
	private static final String g_TAG = "LoginActivity";
	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	
	private PlusClient g_PlusClient;
	private ConnectionResult g_ConnectionResult;
	private ProgressDialog g_ConnectionProgressDialog;
	
	//Facebook override
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_login);
	    
	    //initialize fb helper
	    fb_uiHelper = new UiLifecycleHelper(this, fb_callback);
	    fb_uiHelper.onCreate(savedInstanceState);
	    
	    //initialize google client
	    g_PlusClient = new PlusClient.Builder(this, this, this)
	    	.setActions("http://schemas.google.com/AddActivity")
	    	.setScopes(Scopes.PLUS_LOGIN)
	    	.build();
	    
	    g_ConnectionProgressDialog = new ProgressDialog(this);
	    g_ConnectionProgressDialog.setMessage("Signing In..");
	    
	    findViewById(R.id.button_googleAuth).setOnClickListener(this);
	    
	}
	//Facebook override
	@Override
	public void onResume() {
	    super.onResume();
	    fb_uiHelper.onResume();
	}
	//Facebook override
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    fb_uiHelper.onActivityResult(requestCode, resultCode, data);
	}
	//Facebook override
	@Override
	public void onPause() {
	    super.onPause();
	    fb_uiHelper.onPause();
	}
	//Facebook override
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    fb_uiHelper.onDestroy();
	}
	//Facebook override
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    fb_uiHelper.onSaveInstanceState(outState);
	}
	//facebook connection handler 
	private void fb_onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	    	Intent i = new Intent(LoginActivity.this,
                    ListeningRoom.class);
            startActivity(i);
            finish();
	        Log.i(fb_TAG, "Logged in...");
	    } else if (state.isClosed()) {
	        Log.i(fb_TAG, "Logged out...");
	    	Intent i = new Intent(LoginActivity.this,
                LoginActivity.class);
            startActivity(i);
            finish();
	    }
	}
	//Google override
	public void onConnectionFailed(ConnectionResult result)
	{
	        if (result.hasResolution()) {
	            try {
	                result.startResolutionForResult(this,
	                        REQUEST_CODE_RESOLVE_ERR);
	            } catch (SendIntentException e) {
	                g_PlusClient.connect();
	            }
	        }

		    // Save the intent so that we can start an activity when the user clicks
		    // the sign-in button.
		    g_ConnectionResult = result;
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
	    // We've resolved any connection errors.
		g_ConnectionProgressDialog.dismiss();
		Log.d(g_TAG, "connected");
    	Intent i = new Intent(LoginActivity.this,
                ListeningRoom.class);
        startActivity(i);
        finish();

	}

	@Override
	public void onDisconnected() {
	    Log.d(g_TAG, "disconnected");
    	Intent i = new Intent(LoginActivity.this,
                LoginActivity.class);
        startActivity(i);
        finish();

	}
	
	@Override
	public void onClick(View v) {
	    if (v.getId() == R.id.button_googleAuth
	            && !g_PlusClient.isConnected()) {
	        if(g_ConnectionResult == null)
	        {
	        	g_ConnectionProgressDialog.show();
	        }
	        else
	        {
	            try {
	                g_ConnectionResult.startResolutionForResult(
	                        getParent(), REQUEST_CODE_RESOLVE_ERR);
	            } catch (SendIntentException e) {
	                // Try connecting again.
	                g_ConnectionResult = null;
	                g_PlusClient.connect();
	            }
	        }
	    }
	    
	}


}
