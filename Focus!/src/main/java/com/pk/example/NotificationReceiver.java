package com.pk.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.pk.example.entity.MinNotificationEntity;
import com.pk.example.entity.PreviousNotificationListEntity;
import com.pk.example.entity.ProfileEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kathe on 10/21/2017.
 */

public class NotificationReceiver extends BroadcastReceiver {
    private AppDatabase db;

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.i("intent ","intent "+intent.getExtras().toString());
        db = AppDatabase.getDatabase(context);

        String packageName, title, text;
        packageName = intent.getStringExtra("packageName");
        title = intent.getStringExtra("title");
        text = intent.getStringExtra("text");
        ArrayList<String> profiles = intent.getStringArrayListExtra("profiles");

        new InsertNotification(packageName, title, text, profiles).execute();

//        String temp = intent.getExtras().getString("info")+ "\n----------------------------------------------" + txtView.getText();
//        txtView.setText(temp+"");
    }

    private class InsertNotification extends AsyncTask<Void, Void, Void> {
        String packageName, title, text;
        ArrayList<String> profiles;

        public InsertNotification(String packageName, String title, String text, ArrayList<String> profiles){
            super();
            this.packageName = packageName;
            this.title = title;
            this.text = text;
            this.profiles = profiles;
        }

        @Override
        protected Void doInBackground(Void... params) {
            for(String prof: profiles){
               MinNotificationEntity notif = new MinNotificationEntity(new MinNotification(packageName, title + " --- " + text, new Date(), prof));
                db.minNotificationDao().insert(notif);
//
//                PreviousNotificationListEntity prevList = new PreviousNotificationListEntity();
//                prevList.addNotification(notif);
//                db.previousNotificationListDao().insert(prevList);
            }
//
//            List<MinNotificationEntity> notifs = db.minNotificationDao().loadMinNotificationsFromProfileSync(profiles.get(0));
//            db.previousNotificationListDao().deleteAll();
//            for(MinNotificationEntity not: notifs){
//                PreviousNotificationListEntity prevList = new PreviousNotificationListEntity();
//                prevList.addNotification(not);
//                db.previousNotificationListDao().insert(prevList);
//            }

            return null;
        }


    }


}