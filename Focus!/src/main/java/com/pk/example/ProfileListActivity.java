package com.pk.example;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.widget.ListView;

import com.pk.example.dao.ProfileDao;
import com.pk.example.entity.ProfileEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by williamxu on 10/16/17.
 */

public class ProfileListActivity extends ListActivity {

    private List<ProfileEntity> profileList = null;
    private ProfileListAdapter profileListAdapter = null;
    private ProfileDao profileDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        profileList = profileDao.loadAllProfilesAsync();


        new LoadProfiles().execute();
        ListView listView = getListView();
        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    private class LoadProfiles extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {

            profileListAdapter = new ProfileListAdapter(ProfileListActivity.this,
                    R.layout.profile_list_row, profileList);
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


