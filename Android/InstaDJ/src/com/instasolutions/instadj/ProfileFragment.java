package com.instasolutions.instadj;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

	/** Called when the activity is first created. */
	private GoogleApiClient g_GoogleApiClient;
	private Activity activity;
	private Button btn_logout;
	private ImageView image_profile;
	private TextView text_name;
	private Session fb_session;
    private ProfilePictureView profile_pic;
    private SharedPreferences prefs;

    @Override
	 public View onCreateView(LayoutInflater inflater, 
	            ViewGroup container, 
	            Bundle savedInstanceState) {
			activity = getActivity();
	        View view = inflater.inflate(R.layout.fragment_profile, container, false);
	        return view;
	    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
		btn_logout = (Button) activity.findViewById(R.id.button_Logout);
		image_profile = (ImageView) activity.findViewById(R.id.splash_image);
		text_name = (TextView) activity.findViewById(R.id.text_name);
		fb_session = Session.getActiveSession();
        profile_pic = (ProfilePictureView) activity.findViewById(R.id.image_profile_pic);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        // Display the person's name
        text_name.setText(prefs.getString("FirstName", "Fname") + " " + prefs.getString("LastName", "Lname"));

        // Display the profile picture
        profile_pic.setProfileId(prefs.getString("UserID", ""));
    }

}
