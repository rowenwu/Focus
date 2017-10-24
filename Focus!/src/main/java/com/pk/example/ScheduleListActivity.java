package com.pk.example;

import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import android.widget.ListView;

import com.pk.example.dao.ScheduleDao;
import com.pk.example.entity.ScheduleEntity;

public class ScheduleListActivity extends ListActivity {
//    private PackageManager packageManager = null;
//    private List<ApplicationInfo> applist = null;
//    private AppAdapter listadaptor = null;

//        private ScheduleDao scheduleDao;
    private AppDatabase database;
    private List<ScheduleEntity> scheduleEntityList = null;
//    private List<ApplicationInfo> applist = null;
//    private AppAdapter listadaptor = null;
    private PackageManager packageManager = null;
    private ScheduleAdapter listadaptor = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);

//        packageManager = getPackageManager();

                // cleanup for testing some initial data
//        database.scheduleDao().removeAllUsers();
        // add some data
//
        new LoadApplications().execute();
    }

    public void buttonClicked(View v) {
        Intent i = new Intent(this, ScheduleViewActivity.class);
            i.putExtra("flag", "create");
            startActivity(i);

    }


//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
////        v.setSelected(true);
//        Intent i = new Intent(this, ScheduleViewActivity.class);
//        i.putExtra("flag", "edit");
//        i.putExtra("name", listadaptor.getItem(position).getName());
//        startActivity(i);
//    }



    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {
//            applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
//            applist = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
//
            database = AppDatabase.getDatabase(getApplicationContext());
            scheduleEntityList = database.scheduleDao().loadAllSchedulesSync();
            if (scheduleEntityList.size()==0) {
                //create a fake schedule to insert
                //if no schedules in db
                ScheduleEntity fakeSchedule = new ScheduleEntity(DummyDb.makeFakeSchedule("There are no schedules to display.", 1));

                scheduleEntityList.add(fakeSchedule);
//                database.scheduleDao().insert(fakeSchedule);
//                scheduleEntityList = database.scheduleDao().loadAllSchedulesSync();
            }
//            else {
                listadaptor = new ScheduleAdapter(ScheduleListActivity.this,
                        R.layout.snippet_list_row, scheduleEntityList);
//            }

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(listadaptor);
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ScheduleListActivity.this, null,
                    "Loading schedules...");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}



