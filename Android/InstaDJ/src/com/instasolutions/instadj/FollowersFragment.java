package com.instasolutions.instadj;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

        SparseArray<UserData> followersList = new SparseArray<UserData>();

        // Junk data for testing purposes
        // TODO: Get followers from database
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        UserData user = new UserData(prefs.getString("FirstName", "Fname"), prefs.getString("LastName", "Lname"), prefs.getString("UserID", ""));
        followersList.append(0, user);
        // End of junk data

        followersView.setAdapter(new UserListAdapter(activity, followersList));
    }
}
