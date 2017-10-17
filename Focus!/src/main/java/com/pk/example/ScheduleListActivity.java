package com.pk.example;


import java.util.List;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.pk.example.dao.ScheduleDao;
import com.pk.example.entity.ScheduleEntity;


public class ScheduleListActivity extends ListActivity {
    //private DatabaseHelper dbHelper = null;
    //private SQLiteDatabase db = null;
    private List<ScheduleEntity> scheduleEntityList = null;
    private ScheduleAdapter listadaptor = null;
    private ScheduleDao scheduleDao;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);
        database = AppDatabase.getDatabase(getApplicationContext());

        // cleanup for testing some initial data
//        database.scheduleDao().removeAllUsers();
        // add some data

//        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Create new helper
        // Get the database. If it does not exist, this is where it will also be created.



        new LoadSchedules().execute();
        ListView listView = getListView();
        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }



    private class LoadSchedules extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {
            //get the list of notifications
            //notificationList = db.getNotificationList();
            scheduleEntityList = database.scheduleDao().loadAllSchedulesSync();
            if (scheduleEntityList.size()==0) {
                //create a fake schedule to insert
                //if no schedules in db
                ScheduleEntity fakeSchedule = new ScheduleEntity(DummyDb.makeFakeSchedule("There are no schedules to display.", 5));
                scheduleDao.insert(fakeSchedule);
                scheduleEntityList = database.scheduleDao().loadAllSchedulesSync();
            }
            listadaptor = new ScheduleAdapter(ScheduleListActivity.this,
                    R.layout.schedule_list_row, scheduleEntityList);
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
                    "Loading notifications...");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}


