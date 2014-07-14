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
import com.instasolutions.instadj.util.ServiceGetHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment{

	/** Called when the activity is first created. */
	private GoogleApiClient g_GoogleApiClient;
	private Activity activity;
	private Button btn_logout;
	private ImageView image_profile;
	private TextView text_name;
    private TextView text_score;
	private Session fb_session;
    private ProfilePictureView profile_pic;
    private SharedPreferences prefs;

    @Override
	 public View onCreateView(LayoutInflater inflater, 
	            ViewGroup container, 
	            Bundle savedInstanceState) {
			activity = getActivity();
	        View view = inflater.inflate(R.layout.fragment_profile, container, false);
            activity.getActionBar().setTitle(R.string.title_section7);
	        return view;
	    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
        Bundle args = this.getArguments();
		btn_logout = (Button) activity.findViewById(R.id.button_Logout);
		image_profile = (ImageView) activity.findViewById(R.id.splash_image);
		text_name = (TextView) activity.findViewById(R.id.text_name);
        text_score = (TextView) activity.findViewById(R.id.text_score);
		fb_session = Session.getActiveSession();
        profile_pic = (ProfilePictureView) activity.findViewById(R.id.image_profile_pic);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());


        if (args == null || args.getString("UserID", "").isEmpty()){
            // Display the person's name
            text_name.setText(prefs.getString("FirstName", "Fname") + " " + prefs.getString("LastName", "Lname"));

            // Display the profile picture
            profile_pic.setProfileId(prefs.getString("UserID", ""));

            // Display the person's score
            ServiceGetHelper getHelper = new ServiceGetHelper(){
                @Override
                protected void onPostExecute(String result) {
                    try {
                        JSONObject jProfileInfo = new JSONObject(result);
                        text_score.setText("Score: " + jProfileInfo.getString("score"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            };

            getHelper.execute("http://instadj.amir20001.cloudbees.net/user/get/" + prefs.getString("UserID", "0"));

            // Button event handler
            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Create a confirmation dialog
                    String logout = getResources().getString(R.string.com_facebook_loginview_log_out_action);
                    String cancel = getResources().getString(R.string.com_facebook_loginview_cancel_action);
                    String message = String.format(getResources().getString(R.string.com_facebook_loginview_logged_in_as), prefs.getString("FirstName", "Fname") + " " + prefs.getString("LastName", "Lname"));
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage(message)
                            .setCancelable(true)
                            .setPositiveButton(logout, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    fb_session.closeAndClearTokenInformation();
                                    Intent newIntent = new Intent(activity.getApplicationContext(), LoginActivity.class);
                                    activity.startActivity(newIntent);
                                    activity.finish();
                                }
                            })
                            .setNegativeButton(cancel, null);
                    builder.create().show();
                }
            });
        } else {
            // Display alt person's name
            text_name.setText(args.getString("FirstName", "Fname") + " " + args.getString("LastName", "Lname"));

            // Display alt profile picture
            profile_pic.setProfileId(args.getString("UserID", ""));

            // Display alt person's score
            text_score.setText("Score: " + args.getString("Score"));

            btn_logout.setVisibility(View.INVISIBLE);
        }

    }
}
