package com.instasolutions.instadj;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.instasolutions.instadj.util.ServiceGetHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FollowersFragment extends Fragment{

    Activity activity = null;
    ListView followersView = null;
    SparseArray<UserData> followersList = new SparseArray<UserData>();

    /** Called when the activity is first created. */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        activity = this.getActivity();
        View view = inflater.inflate(R.layout.fragment_followers, container, false);

        activity.getActionBar().setTitle(R.string.title_section6);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        followersView = (ListView) activity.findViewById(R.id.followers_list);
        final ProgressBar pbar = (ProgressBar)activity.findViewById(R.id.followers_progress);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        followersList.clear();
        followersView.setAdapter(new UserListAdapter(activity, followersList));
        pbar.setVisibility(ProgressBar.VISIBLE);

        ServiceGetHelper getHelper = new ServiceGetHelper(){
            @Override
            protected void onPostExecute(String result) {
                try {
                    JSONArray jFollowersArray = new JSONArray(result);
                    for(int i = 0; i <jFollowersArray.length(); i++){
                        final JSONObject jPlaylist = jFollowersArray.getJSONObject(i);
                        final String userID = jPlaylist.getString("userId");
                        final String fName = jPlaylist.getString("firstName");
                        final String lName = jPlaylist.getString("lastName");
                        final String score = jPlaylist.getString("score");

                        final UserData user = new UserData(fName, lName, userID, score);

                        followersList.append(i, user);

                        followersView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                UserData user = (UserData) followersView.getItemAtPosition(i);
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
                followersView.setAdapter(new UserListAdapter(activity, followersList));
                pbar.setVisibility(ProgressBar.INVISIBLE);

            }

        };

        getHelper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
    			"http://instadj.amir20001.cloudbees.net/follow/get/followers/" + prefs.getString("UserID", "0"));
    }
}
