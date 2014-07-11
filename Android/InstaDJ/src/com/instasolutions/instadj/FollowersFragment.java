package com.instasolutions.instadj;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.List;

public class FollowersFragment extends Fragment{

    Activity activity = null;
    ListView followersView = null;

    /** Called when the activity is first created. */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        activity = this.getActivity();
        View view = inflater.inflate(R.layout.fragment_followers, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        followersView = (ListView) activity.findViewById(R.id.followers_list);

        List<ProfileData> followersList = new ArrayList<ProfileData>();

        // Junk data for testing purposes
        // TODO: Get followers from database
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        followersList.add(new ProfileData(prefs.getString("UserID", ""), prefs.getString("FirstName", "Fname") + " " + prefs.getString("LastName", "Lname"), ""));

        followersView.setAdapter(new FollowListAdapter(activity.getApplicationContext(), followersList));
    }
}
