package com.pk.example.clientui;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.pk.example.R;
import com.pk.example.database.AppDatabase;
import com.pk.example.entity.ProfileEntity;
import com.pk.example.entity.ScheduleEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by williamxu on 10/16/17.
 */

public class ProfileListActivity extends ListActivity {

    private List<ProfileEntity> profileList = null;
    private ProfileListAdapter profileListAdapter = null;
    private AppDatabase db;
    TextView textView;
    //ListView profileListView;
    Button profilesButton;
    ToggleButton sortToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        ListView profileListView = getListView();
        textView=(TextView)findViewById(R.id.textView);
        profilesButton = (Button) findViewById( R.id.btnToast );

        sortToggle = (ToggleButton) findViewById(R.id.sortToggle);
        sortToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    new SortProfiles().execute();
                } else {
                    // The toggle is disabled
                    new LoadProfiles().execute();
                }
            }
        });

        new LoadProfiles().execute();
    }

    public void buttonClicked(View v) {
        Intent i = new Intent(getApplicationContext(), ProfileViewActivity.class);
        i.putExtra("flag", "create");
        startActivity(i);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent i = new Intent(getApplicationContext(), ProfileViewActivity.class);
        i.putExtra("flag", "view");
        i.putExtra("name", profileListAdapter.getItem(position).getName());
        startActivity(i);
    }


    private class LoadProfiles extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {

            db = AppDatabase.getDatabase(getApplicationContext());
            profileList = db.profileDao().loadAllProfilesAsync();

            //create a fake schedule to insert if no schedules in db
            if (profileList.size()==0) {
                ProfileEntity fakeProfile = new ProfileEntity(new Profile("There are no profiles to display.",
                        new ArrayList<>( Arrays.asList("Buenos Aires", "Córdoba", "La Plata")), false));
                profileList.add(fakeProfile);
            }

            profileListAdapter = new ProfileListAdapter(ProfileListActivity.this,
                    R.layout.schedule_list_row, profileList);

            return null;

        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(profileListAdapter);
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ProfileListActivity.this, null,
                    "Loading profiles...");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    private class SortProfiles extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {

            db = AppDatabase.getDatabase(getApplicationContext());
            profileList = db.profileDao().loadAllProfilesAsync();
            List<ScheduleEntity> scheduleList = db.scheduleDao().loadAllSchedulesSync();

            //create a fake schedule to insert if no schedules in db
            if (profileList.size()==0) {
                ProfileEntity fakeProfile = new ProfileEntity(new Profile("There are no profiles to display.",
                        new ArrayList<>( Arrays.asList("Buenos Aires", "Córdoba", "La Plata")), false));
                profileList.add(fakeProfile);
            }

            ProfileSorter sorter = new ProfileSorter(profileList, scheduleList);
            List<ProfileEntity> sortedProfileList = sorter.getSortedProfiles();

            profileListAdapter = new ProfileListAdapter(ProfileListActivity.this,
                    R.layout.schedule_list_row, sortedProfileList);

            return null;

        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(profileListAdapter);
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ProfileListActivity.this, null,
                    "Loading profiles...");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


}

