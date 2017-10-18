package com.pk.example;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.pk.example.dao.ProfileDao;
import com.pk.example.entity.ProfileEntity;

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        textView=(TextView)findViewById(R.id.textView);

        new LoadProfiles().execute();
//        ListView listView = getListView();
        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

//    public void setEmptyState(){
//        textView.setText("No profiles to display.");
//    }

    public void buttonClicked(View v) {
        Intent i = new Intent(this, ProfileViewActivity.class);
        i.putExtra("flag", "create");
        startActivity(i);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
//        v.setSelected(true);
        Intent i = new Intent(this, ProfileViewActivity.class);
        i.putExtra("flag", "edit");
        i.putExtra("name", profileListAdapter.getItem(position).getName());
        startActivity(i);
    }

    private class LoadProfiles extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {

            db = AppDatabase.getDatabase(getApplicationContext());
            profileList = db.profileDao().loadAllProfilesAsync();
            if (profileList.size()==0) {
//                setEmptyState();
                //create a fake schedule to insert
                //if no schedules in db
                ProfileEntity fakeProfile = new ProfileEntity(new Profile("There are no profiles to display.", new ArrayList<>( Arrays.asList("Buenos Aires", "Córdoba", "La Plata")), false));
//                db.profileDao().insert(fakeProfile);
//                profileList = db.profileDao().loadAllProfilesAsync();
                profileList.add(fakeProfile);
            }
//            else {

                profileListAdapter = new ProfileListAdapter(ProfileListActivity.this,
                        R.layout.schedule_list_row, profileList);

//            }
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

