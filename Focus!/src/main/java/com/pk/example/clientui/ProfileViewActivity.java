package com.pk.example.clientui;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pk.example.R;
import com.pk.example.database.dao.ProfileDao;
import com.pk.example.database.AppDatabase;
import com.pk.example.entity.ProfileEntity;
import com.pk.example.servicereceiver.ProfileScheduler;


public class ProfileViewActivity extends ListActivity {
    private AppAdapter listadaptor = null;
    private List<ProfileEntity> profileList = null;
    private String name;

    private AppDatabase db;

    private String flag;
    private ProfileEntity profileEntity; // for edit/delete
    ProfileEntity profileInsert;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getDatabase(getApplicationContext());
        flag = getIntent().getStringExtra("flag");

        //load all profiles for name checking
        new LoadAllProfiles().execute();

        // create profile mode
        if (flag.equals("create")) {
            setContentView(R.layout.activity_create_profile);
            getActionBar().setDisplayHomeAsUpEnabled(true);


            new LoadApplications().execute();

        }
        // edit/delete profile mode
        else if (flag.equals("edit")) {
            name = getIntent().getStringExtra("name");
            setContentView(R.layout.activity_profile);
            getActionBar().setDisplayHomeAsUpEnabled(true);

            new LoadApplications().execute();
//            ListView listView = getListView();
//            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            // populate editTextProfileName with current profile name
            EditText profileName = (EditText) findViewById(R.id.editTextProfileName);
            profileName.setText(name);

//            ArrayList<String> appsToBlock = profileEntity.getAppsToBlock();
//            for(int i = 0; i < appsToBlock.size(); i++){
//                int position = listadaptor.getItemPosition(appsToBlock.get(i));
//                if(position != -1)
//                    getListView().setItemChecked(position, listadaptor.changeCheckedState(position));
//            }

        }
        // view profile mode
        else if (flag.equals("view")) {
            name = getIntent().getStringExtra("name");
//            profileEntity = db.profileDao().loadProfileSync(name);
            setContentView(R.layout.activity_profile_view);
            getActionBar().setDisplayHomeAsUpEnabled(true);

            new LoadProfileApplications().execute();

            TextView profileName = (TextView) findViewById(R.id.profileNameTextView);
            profileName.setText(name);

            //  populate listview with apps currently in profile
//            for (int i = 0; i < applist.size(); i++) {
//                if (profileEntity.getAppsToBlock().contains(applist.get(i).packageName)) {
//                    listView.setItemChecked(i, true);
//                }
//            }
        }

        ListView listView = getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        getListView().setItemChecked(position, listadaptor.changeCheckedState(position));
    }


    public String createButtonClicked(View v){
        // get new profile name from text field
        EditText profileName = (EditText) findViewById(R.id.editTextProfileName);
        String pname = profileName.getText().toString();

        // get selected apps
        ArrayList<String> appPacks = listadaptor.getSelectedApps();

        if (pname.isEmpty()) {
            // show toast if profile name is empty
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Please enter a profile name", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (appPacks.size() == 0) {
            // show toast if no apps are selected
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Please select one or more apps", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (NameExists(pname)){
            // show toast if name already exist
            Toast toast = Toast.makeText(getApplicationContext(),
                    "This name has been taken. Please choose another one", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            // add profile to db, return to ProfileListActivity
            new InsertProfile().execute();
            profileInsert = new ProfileEntity(new Profile(pname, appPacks, true));

            // notify user
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Profile created", Toast.LENGTH_SHORT);
            toast.show();

            flag = null;
            // return to ProfileListActivity
            Intent i = new Intent(this, ProfileListActivity.class);
            startActivity(i);
        }
        return flag;
    }

    public String saveButtonClicked(View v) {
        // get new profile name from text field
        EditText profileName = (EditText) findViewById(R.id.editTextProfileName);
        String pname = profileName.getText().toString();

        // get selected apps
        ArrayList<String> appPacks = listadaptor.getSelectedApps();

        if (pname.isEmpty()) {
            // show toast if profile name is empty
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Name cannot be blank", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (appPacks.size() == 0) {
            // show toast if no apps are selected
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Please select one or more apps", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (NameExistsEditMode(pname))
        {
            // show toast if name already exist
            Toast toast = Toast.makeText(getApplicationContext(),
                    "This name has been taken. Please choose another one", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            // update profile and return to profile list view

            //if profile is active, change apps blocked by NLService
            boolean wasActive = false;
            if(profileEntity.getActive()){
                ProfileScheduler.turnOffProfile(getApplicationContext(), profileEntity);
                wasActive = true;
            }

            profileEntity.setName(pname);
            profileEntity.setAppsToBlock(appPacks);

            new UpdateProfile().execute();
            if(wasActive){
                ProfileScheduler.turnOnProfile(getApplicationContext(), profileEntity);
            }

            // notify user
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Profile updated", Toast.LENGTH_SHORT);
            toast.show();

            flag = null;
            // return to ProfileListActivity
            Intent i = new Intent(this, ProfileListActivity.class);
            startActivity(i);
        }
        return flag;
    }

    public String deleteButtonClicked(View v) {
        // delete profile and return to profile list view
        //profileDao.delete(profileEntity);
        if(profileEntity.getActive())
            ProfileScheduler.turnOffProfile(getApplicationContext(), profileEntity);
        new DeleteProfile(name).execute();

        // notify user
        Toast toast = Toast.makeText(getApplicationContext(),
                "Profile deleted", Toast.LENGTH_SHORT);
        toast.show();


        // TODO if active, remove from NLService
//            if(profileEntity.getActive()){
//                ProfileScheduler.turnOffProfile(this, pname);
//            }
        flag = null;
        // return to ProfileListActivity
        Intent i = new Intent(this, ProfileListActivity.class);
        startActivity(i);

        return flag;
    }

    // edit button on profile view
    public String editButtonClicked(View v) {
        Intent i = new Intent(getApplicationContext(), ProfileViewActivity.class);
        i.putExtra("flag", "edit");
        i.putExtra("name", name);
//        profileEntity = db.profileDao().loadProfileSync(name);
        startActivity(i);
        return flag;
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            List<ApplicationInfo> installedApps = getInstalledApps();

            listadaptor = new AppAdapter(ProfileViewActivity.this,
                    R.layout.snippet_list_row, installedApps);

            if(name != null) {
                profileEntity = db.profileDao().loadProfileSync(name);
            }

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(listadaptor);
            super.onPostExecute(result);
        }

    }

    public boolean isSystemPackage(ApplicationInfo applicationInfo) {
        return ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public List<ApplicationInfo> getInstalledApps(){
        final PackageManager packageManager = getPackageManager();

        List<ApplicationInfo> applist = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        List<ApplicationInfo> installedApps = new ArrayList<ApplicationInfo>();
        for(ApplicationInfo ai: applist){
            if(!isSystemPackage(ai))
                installedApps.add(ai);
        }
        return installedApps;

    }

    private class LoadProfileApplications extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if(name != null) {
                profileEntity = db.profileDao().loadProfileSync(name);
            }

            List<ApplicationInfo> installedApps = getInstalledApps();
            ArrayList<ApplicationInfo> profileAppList = new ArrayList<>();

            for (ApplicationInfo ai : installedApps) {
                if (profileEntity.getAppsToBlock().contains(ai.packageName)) {
                    profileAppList.add(ai);
                }
            }

            listadaptor = new AppAdapter(ProfileViewActivity.this,
                    R.layout.snippet_list_row, profileAppList);

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(listadaptor);
            super.onPostExecute(result);
        }

    }

    private class InsertProfile extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {

            db.profileDao().insert(profileInsert);

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ProfileViewActivity.this, null,
                    "Saving");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    private class UpdateProfile extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {

            db.profileDao().update(profileEntity);
            profileEntity = db.profileDao().loadProfileSync(profileEntity.getId());

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ProfileViewActivity.this, null,
                    "Saving");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    private class DeleteProfile extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;
        private String name;
        private ProfileEntity profile;

        DeleteProfile(String name) {
            this.name = name;
            profile = db.profileDao().loadProfileSync(name);
        }

        @Override
        protected Void doInBackground(Void... params) {

            db.profileDao().delete(profile);

            return null;
        }

    }

    private class LoadAllProfiles extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {
            db = AppDatabase.getDatabase(getApplicationContext());
            profileList = db.profileDao().loadAllProfilesAsync();
            return null;
        }

    }

    private boolean NameExists(String pname)
    {
        if (profileList.size() == 0)
        {
            return false;
        } else {
            for (int i = 0 ; i < profileList.size(); ++i)
            {
                if (profileList.get(i).getName().equals(pname))
                {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean NameExistsEditMode(String pname)
    {
        if (profileList.size() == 0)
        {
            return false;
        } else {
            for (int i = 0 ; i < profileList.size(); ++i)
            {
                if (profileList.get(i).getName().equals(pname) && (!pname.equals(name)))
                {
                    return true;
                }
            }
            return false;
        }
    }


    // for testing
    public String getFlag() {
        return flag;
    }
}