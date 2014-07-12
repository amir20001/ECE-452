package com.instasolutions.instadj;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.instasolutions.instadj.util.ServicePostHelper;

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
	private static final String g_TAG = "LoginActivity";
	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	private static final int RC_SIGN_IN = 0;
	
	private boolean g_SignInClicked;
	private boolean g_IntentInProgress;
	
	private GoogleApiClient g_GoogleApiClient;
	private ConnectionResult g_ConnectionResult;
	
	private SignInButton g_signInButton;
	
	//Facebook override
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_login);
	    
	    //initialize fb helper
	    fb_uiHelper = new UiLifecycleHelper(this, fb_callback);
	    fb_uiHelper.onCreate(savedInstanceState);
	    
	    //initialize google client
	    g_signInButton = (SignInButton) findViewById(R.id.button_googleAuth);
	    g_signInButton.setOnClickListener(this);
	    
	    g_GoogleApiClient = new GoogleApiClient.Builder(this)
        		.addConnectionCallbacks(this)
    			.addOnConnectionFailedListener(this)
    			.addApi(Plus.API)
    			.addScope(Plus.SCOPE_PLUS_LOGIN).build();
	    	
	    
	}
	//Facebook override
	@Override
	public void onResume() {
	    super.onResume();
	    fb_uiHelper.onResume();
	}
	//Facebook/Google override
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    //Facebook
	    fb_uiHelper.onActivityResult(requestCode, resultCode, data);
	    //Google
	    if(requestCode == RC_SIGN_IN){
	    	if(resultCode != RESULT_OK){
	    		g_SignInClicked = false;
	    	}
	    	
	    	g_IntentInProgress = false;
	    	
	    	if(!g_GoogleApiClient.isConnecting()){
	    		g_GoogleApiClient.connect();
	    	}
	    }
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
	@SuppressWarnings("deprecation")
	private void fb_onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
            Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        SharedPreferences.Editor prefEdit = prefs.edit();
                        prefEdit.putString("FirstName", user.getFirstName());
                        prefEdit.putString("LastName", user.getLastName());
                        prefEdit.putString("UserID", user.getId());
                        prefEdit.commit();
                        
                        UserData ud = new UserData(user.getFirstName(), user.getLastName(), user.getId());
                        
                        try{
                        	JSONObject jud = new JSONObject();
                        	jud.put("userId", ud.getUserID());
                        	jud.put("firstName", ud.getFirstName());
                        	jud.put("lastName", ud.getLastName());
                        	ServicePostHelper post = new ServicePostHelper();
                        	post.execute("http://instadj.amir20001.cloudbees.net/user/insert",jud.toString());
                        }catch (Exception e){
                        	Log.e("instaDJ", "JSONException", e);
                        }
                       
                        Log.i(fb_TAG, user.getFirstName() + " " + user.getLastName());
                    }
                }
            });
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
	protected void onStart(){
		super.onStart();
		g_GoogleApiClient.connect();
	}
	
	//Google override
	protected void onStop(){
		super.onStop();
		if(g_GoogleApiClient.isConnected()){
			g_GoogleApiClient.disconnect();
		}
	}
	
	//Google override
	public void onConnectionFailed(ConnectionResult result)
	{
	        if (!result.hasResolution()) {
	            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
	            return;
	        }
	        
	        if(!g_IntentInProgress){
	        	g_ConnectionResult = result;
	        	
	        	if(g_SignInClicked){
	        		resolveSignInErrors();
	        	}
	        }

		    // Save the intent so that we can start an activity when the user clicks
		    // the sign-in button.
		    g_ConnectionResult = result;
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
	    // We've resolved any connection errors.
		Log.d(g_TAG, "connected");
		g_SignInClicked = false;



    	Intent i = new Intent(LoginActivity.this,
                ListeningRoom.class);
        startActivity(i);
        finish();

	}
	
	@Override
	public void onConnectionSuspended(int arg0){
		g_GoogleApiClient.connect();
	}
	
	//On-Click override for Google sign-in button
	@Override
	public void onClick(View v) {
	    if (v.getId() == R.id.button_googleAuth)
	    {
	    	if(!g_GoogleApiClient.isConnecting()){
	    		g_SignInClicked = true;
	    		resolveSignInErrors();
	    	}
	    }
	    
	}
	//Resolve sign-in errors for google sign-in
	private void resolveSignInErrors()
	{
		if(g_ConnectionResult.hasResolution()){
			try{
				g_IntentInProgress = true;
				g_ConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
			} catch (SendIntentException e){
				g_IntentInProgress = false;
				g_GoogleApiClient.connect();
			}
		}
	}


}
