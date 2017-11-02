package com.pk.example.clientui;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.pk.example.R;
import com.pk.example.database.AppDatabase;
import com.pk.example.entity.ProfileEntity;
import com.pk.example.servicereceiver.ProfileScheduler;

import java.util.List;


/**
 * Created by williamxu on 10/16/17.
 */

public class ProfileListAdapter extends ArrayAdapter<ProfileEntity> {
    private List<ProfileEntity> profileList = null;
    private Context context;
    ToggleButton b;
    private AppDatabase database;

    public ProfileListAdapter(Context context, int textViewResourceId,
                              List<ProfileEntity> profileList) {
        super(context, textViewResourceId, profileList);
        this.context = context;
        this.profileList = profileList;
        database = AppDatabase.getDatabase(context);

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.schedule_list_row, null);
        }

        final ProfileEntity profileEntity = profileList.get(position);
        if (null != profileEntity) {
            TextView profileContext = (TextView) view.findViewById(R.id.name);
            profileContext.setText(profileEntity.getName());
            b = (ToggleButton) view.findViewById(R.id.toggBtn);

            if(profileEntity.getName().equals("There are no profiles to display.")){
                ((ViewGroup) b.getParent()).removeView(b);

            }
            else {
                if(profileEntity.getActive()){
                    b.setChecked(true);
                }
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (b.isChecked()) {
                            ProfileScheduler.turnOnProfile(context, getItem(position));
                            profileEntity.setActive(true);
                        } else {
                            ProfileScheduler.turnOffProfile(context, getItem(position));
                            profileEntity.setActive(false);
                        }
//                        new UpdateProfile(profileEntity).execute();
                    }
                });

            }

        }
        return view;
    }

    private class UpdateProfile extends AsyncTask<Void, Void, Void> {
        private ProfileEntity profile;

        public UpdateProfile(ProfileEntity profile ){
            super();
            this.profile = profile;
        }

        @Override
        protected Void doInBackground(Void... params) {
            database.profileDao().update(profile);
            return null;
        }
    }
}