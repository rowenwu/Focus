package com.pk.example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pk.example.entity.ProfileEntity;

import java.util.List;


/**
 * Created by williamxu on 10/16/17.
 */

public class ProfileListAdapter extends ArrayAdapter<ProfileEntity> {
    private List<ProfileEntity> profileList = null;
    private Context context;

    public ProfileListAdapter(Context context, int textViewResourceId,
                               List<ProfileEntity> profileList) {
        super(context, textViewResourceId, profileList);
        this.context = context;
        this.profileList = profileList;
    }

    @Override
    public int getCount() {
        return ((null != profileList) ? profileList.size() : 0);
    }

    @Override
    public ProfileEntity getItem(int position) {
        return ((null != profileList) ? profileList.get(position) : null);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.profile_list_row, null);
        }

        ProfileEntity profileEntity = profileList.get(position);
        if (null != profileEntity) {
            TextView profileContext = (TextView) view.findViewById(R.id.profile_list_content);

            profileContext.setText(profileEntity.getName());
            //still need to set info
            //appName.setText(applicationInfo.loadLabel(packageManager));
            //packageName.setText(applicationInfo.packageName);
            //iconview.setImageDrawable(applicationInfo.loadIcon(packageManager));
        }
        return view;
    }
}
