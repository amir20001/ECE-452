package com.instasolutions.instadj;

import com.instasolutions.instadj.util.ServiceGetHelper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class FollowingFragment extends Fragment{

    Activity activity = null;
    ListView followingView = null;

    /** Called when the activity is first created. */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        activity = this.getActivity();
        View view = inflater.inflate(R.layout.fragment_following, container, false);

        activity.getActionBar().setTitle(R.string.title_section5);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        followingView = (ListView) activity.findViewById(R.id.following_list);

        SparseArray<UserData> followingList = new SparseArray<UserData>();

        // Junk data for testing purposes
        // TODO: Get followers from database
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        UserData user = new UserData(prefs.getString("FirstName", "Fname"), prefs.getString("LastName", "Lname"), prefs.getString("UserID", ""));
        followingList.append(0, user);
        // End of junk data

        followingView.setAdapter(new UserListAdapter(activity, followingList));
    }
}
