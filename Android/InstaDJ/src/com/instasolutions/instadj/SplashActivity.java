package com.instasolutions.instadj;

import com.facebook.Session;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

public class SplashActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        //ImageView logoImage = (ImageView)findViewById(R.id.splash_image);
        WebView loadAnimation = (WebView)findViewById(R.id.splash_load);
        loadAnimation.loadUrl("file:///android_asset/splash.html");
        loadAnimation.setBackgroundColor(0x00000000);
        //logoImage.setImageResource(R.drawable.logo_animation);
        //AnimationDrawable logoAnimation = (AnimationDrawable)logoImage.getDrawable();
       // logoAnimation.start();

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(5000);  //Delay of 5 seconds
                } catch (Exception e) {

                } finally {
                	Session session = Session.getActiveSession();
                	Intent i = null;
                	if(session != null){
	                    i = new Intent(SplashActivity.this,
	                            ListeningRoom.class);
                	}
                	else
                	{
                		i = new Intent(SplashActivity.this,
                				 LoginActivity.class);
                	}
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }

}
