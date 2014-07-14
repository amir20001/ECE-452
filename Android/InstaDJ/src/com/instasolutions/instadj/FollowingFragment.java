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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FollowingFragment extends Fragment{

    Activity activity = null;
    ListView followingView = null;
    SparseArray<UserData> followingList = new SparseArray<UserData>();

    /** Called when the activity is first created. */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        activity = this.getActivity();
        activity.getActionBar().setTitle(R.string.title_section5);
        View view = inflater.inflate(R.layout.fragment_following, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        followingView = (ListView) activity.findViewById(R.id.following_list);
        final ProgressBar pbar = (ProgressBar)activity.findViewById(R.id.following_progress);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        pbar.setVisibility(ProgressBar.VISIBLE);

        ServiceGetHelper getHelper = new ServiceGetHelper(){
            @Override
            protected void onPostExecute(String result) {
                try {
                    JSONArray jFollowingArray = new JSONArray(result);
                    for(int i = 0; i < jFollowingArray.length(); i++){
                        JSONObject jPlaylist =  jFollowingArray.getJSONObject(i);
                        final String userID = jPlaylist.getString("userId");
                        final String fName = jPlaylist.getString("firstName");
                        final String lName = jPlaylist.getString("lastName");
                        final String score = jPlaylist.getString("score");

                        final UserData user = new UserData(fName, lName, userID, score);

                        followingList.append(i, user);

                        followingView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                UserData user = (UserData) followingView.getItemAtPosition(i);
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                ProfileFragment profileFrag = new ProfileFragment();
                                Bundle args = new Bundle();
                                args.putString("UserID", user.getUserID());
                                args.putString("FirstName", user.getFirstName());
                                args.putString("LastName", user.getLastName());
                                args.putString("Score", user.getScore());
                                profileFrag.setArguments(args);
                                fragmentTransaction.replace(R.id.container, profileFrag, "ProfileFragment");
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                followingView.setAdapter(new UserListAdapter(activity, followingList));
                pbar.setVisibility(ProgressBar.INVISIBLE);

            }

        };

        getHelper.execute("http://instadj.amir20001.cloudbees.net/follow/get/followees/" + prefs.getString("UserID", "0"));
    }
}
