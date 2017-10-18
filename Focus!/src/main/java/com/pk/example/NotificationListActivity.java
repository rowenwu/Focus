package com.pk.example;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Fragment;
import android.app.ListActivity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.pk.example.dao.PreviousNotificationListDao;
import com.pk.example.dao.ScheduleDao;
import com.pk.example.entity.MinNotificationEntity;
import com.pk.example.entity.PreviousNotificationListEntity;
import com.pk.example.entity.ScheduleEntity;

public class NotificationListActivity extends ListActivity {

    private PreviousNotificationListDao previousNotificationListDao;
    private AppDatabase database;
    private List<PreviousNotificationListEntity> previousNotificationListEntityList;
    private List<MinNotificationEntity> minNotificationEntityList = null;
    private PackageManager packageManager = null;
    private NotificationAdapter listadaptor = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        //creates view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        minNotificationEntityList = new ArrayList<MinNotificationEntity>();
        new LoadApplications().execute();
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("startBackground", "startBackground");
            //get database
            database = AppDatabase.getDatabase(getApplicationContext());
            //get the list of previousNotificationListEntity
            previousNotificationListEntityList = database.previousNotificationListDao().loadAllPrevNotifications();
            //if there is no previousNotificationList yet create one
            if (previousNotificationListEntityList.size()==0) {
                //create a fake notification to insert
                //if no notification in db
                PreviousNotificationListEntity fakePreviousNotificationListEntity = new PreviousNotificationListEntity();
                //create a fake minNotificaiton to add to the list
                Date date = Calendar.getInstance().getTime();
                MinNotificationEntity fakeNotification = new MinNotificationEntity(new MinNotification("fake", "This notification is to show test", date));

                //minNotificationEntityList.add(fakeNotification);



                //add the fake minNotification to the previousNotificationList
                fakePreviousNotificationListEntity.addNotification(fakeNotification);
                //add the list
                database.previousNotificationListDao().insert(fakePreviousNotificationListEntity);
                //get the list back
                previousNotificationListEntityList = database.previousNotificationListDao().loadAllPrevNotifications();
            }

            //minNotificationEntityList =  previousNotificationListEntityList.get(0).getNotificationList();

            listadaptor = new NotificationAdapter(NotificationListActivity.this,
                    R.layout.notification_list_row, minNotificationEntityList);

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
            progress = ProgressDialog.show(NotificationListActivity.this, null,
                    "Loading application info...");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
