package com.pk.example.clientui;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.pk.example.R;
import com.pk.example.database.AppDatabase;
import com.pk.example.entity.PrevNotificationEntity;
import com.pk.example.entity.ProfileEntity;
import com.pk.example.entity.ScheduleEntity;
import com.pk.example.servicereceiver.NLService;

public class NotificationListActivity extends ListActivity {

    private AppDatabase database;

    private PackageManager packageManager = null;
    private NotificationAdapter listadaptor = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //creates view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);

//        minNotificationEntityList = new ArrayList<MinNotificationEntity>();

        new LoadApplications().execute();



    }
    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {
            //get database
            database = AppDatabase.getDatabase(getApplicationContext());

            //test if notifications are in minnotificationlist
//            List<MinNotificationEntity> nots = database.minNotificationDao().loadMinNotificationsFromProfileSync("profile");
//            listadaptor = new NotificationAdapter(NotificationListActivity.this,
//                    R.layout.notification_list_row, nots);

//            database.prevNotificationDao().deleteAll();
//            List<MinNotificationEntity> minNotifs = database.minNotificationDao().loadMinNotificationsFromProfileSync("profile");
//            for(MinNotificationEntity notif: minNotifs){
//                MinNotification mn = new MinNotification(notif.getAppName(), notif.getNotificationContext(), notif.getDate(), notif.getProfileName());
//                database.prevNotificationDao().insert(new PrevNotificationEntity(mn));
//            }

            List<PrevNotificationEntity> notifs = database.prevNotificationDao().loadAllPrevNotificationsSync();
            if (notifs == null || notifs.size()==0) {
                database.prevNotificationDao().insert(new PrevNotificationEntity(new MinNotification("There are no notifications to display", "", new Date(), "")));
                notifs = database.prevNotificationDao().loadAllPrevNotificationsSync();
            }


            listadaptor = new NotificationAdapter(NotificationListActivity.this,
                    R.layout.notification_list_row, notifs);

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
                    "Loading notifications...");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


}
