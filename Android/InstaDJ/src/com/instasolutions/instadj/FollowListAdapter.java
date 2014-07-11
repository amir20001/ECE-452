package com.instasolutions.instadj;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

import java.util.List;

public class FollowListAdapter extends ArrayAdapter<ProfileData> {

    Context context;
    List<ProfileData> objects;

    public FollowListAdapter(Context context, List<ProfileData> objects) {
        super(context, R.layout.list_row_users, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_row_users, parent, false);
        TextView userName = (TextView) rowView.findViewById(R.id.user_name);
        ProfilePictureView profilePic = (ProfilePictureView) rowView.findViewById(R.id.list_user_picture);

        if (!objects.isEmpty()){
            userName.setText(objects.get(position).getUserName());
            profilePic.setProfileId(objects.get(position).getProfilePic());
        } else {
            // TODO: Add else condition
        }

        return rowView;
    }
}
