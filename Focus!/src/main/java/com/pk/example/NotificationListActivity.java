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
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.pk.example.dao.ScheduleDao;
import com.pk.example.entity.MinNotificationEntity;
import com.pk.example.entity.PrevNotificationEntity;
import com.pk.example.entity.PreviousNotificationListEntity;
import com.pk.example.entity.ScheduleEntity;

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

            database.prevNotificationDao().deleteAll();
            List<MinNotificationEntity> minNotifs = database.minNotificationDao().loadMinNotificationsFromProfileSync("profile");
            for(MinNotificationEntity notif: minNotifs){
                MinNotification mn = new MinNotification(notif.getAppName(), notif.getNotificationContext(), notif.getDate(), notif.getProfileName());
                database.prevNotificationDao().insert(new PrevNotificationEntity(mn));
            }

            List<PrevNotificationEntity> notifs = database.prevNotificationDao().loadAllPrevNotificationsSync();
            if (notifs == null || notifs.size()==0) {
                database.prevNotificationDao().insert(new PrevNotificationEntity(new MinNotification("", "There are no notifications to display", new Date(), "")));
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
