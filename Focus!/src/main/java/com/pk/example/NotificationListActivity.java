package com.pk.example;

import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Andy on 10/6/2017.
 */

public class NotificationListActivity extends ListActivity {
    //private DatabaseHelper dbHelper = null;
    //private SQLiteDatabase db = null;
    private List<Notification> notificationList = null;
    private NotificationAdapter listadaptor = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Create new helper
        //dbHelper = new DatabaseHelper(getContext());
        // Get the database. If it does not exist, this is where it will also be created.
        //db = dbHelper.getReadableDatabase();//if you're just pulling data

        new LoadNotifications().execute();
        ListView listView = getListView();
        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    private class LoadNotifications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {
            //get the list of notifications
            //notificationList = db.getNotificationList();

            listadaptor = new NotificationAdapter(NotificationListActivity.this,
                    R.layout.notification_list_row, notificationList);
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


