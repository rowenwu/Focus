package com.pk.example.clientui;

import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.pk.example.R;
import com.pk.example.database.AppDatabase;
import com.pk.example.database.DummyDb;
import com.pk.example.entity.ScheduleEntity;

public class ScheduleListActivity extends ListActivity {
    private AppDatabase database;
    private List<ScheduleEntity> scheduleEntityList = null;
    private PackageManager packageManager = null;
    private ScheduleAdapter listadaptor = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        new LoadSchedules().execute();
    }

    public void buttonClicked(View v) {
        Intent i = new Intent(this, ScheduleViewActivity.class);
            i.putExtra("flag", "create");
            startActivity(i);

    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
//        v.setSelected(true);
        Intent i = new Intent(this, ScheduleViewActivity.class);
        i.putExtra("flag", "view");
        i.putExtra("id", listadaptor.getItem(position).getId());
        startActivity(i);
    }



    private class LoadSchedules extends AsyncTask<Void, Void, Void> {
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
            }
            listadaptor = new ScheduleAdapter(ScheduleListActivity.this,
                    R.layout.snippet_list_row, scheduleEntityList);

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(listadaptor);
            super.onPostExecute(result);
        }
    }
}



