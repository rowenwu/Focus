package com.pk.example;


import java.util.List;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;


/**
 * Created by Andy on 10/6/2017.
 */

public class NotificationListActivity extends ListActivity {
    //private DatabaseHelper dbHelper = null;
    //private SQLiteDatabase db = null;
    private List<MinNotification> notificationList = null;
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


