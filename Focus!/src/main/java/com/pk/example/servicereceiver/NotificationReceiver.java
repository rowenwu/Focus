package com.pk.example.servicereceiver;

import android.arch.persistence.room.Update;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.pk.example.database.AppDatabase;
import com.pk.example.clientui.MinNotification;
import com.pk.example.entity.MinNotificationEntity;
import com.pk.example.entity.PrevNotificationEntity;
import com.pk.example.entity.ProfileEntity;
import com.pk.example.entity.ScheduleEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pk.example.servicereceiver.NLService.*;


/**
 * Created by kathe on 10/21/2017.
 */

public class NotificationReceiver extends BroadcastReceiver {
    private AppDatabase db;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.i("intent ","intent "+intent.getExtras().toString());
        db = AppDatabase.getDatabase(context);
        ArrayList<String> profiles;
        String name;
        this.context = context;
        Intent i;
        ArrayList<String> profs = profiles = new ArrayList<String>();


        switch(intent.getAction()) {
            case TOGGLE_SCHEDULE:
                //testing
                profs.add("profile");

                i = new Intent(INSERT_NOTIFICATION);
                // Make an intent
                boolean active = intent.getBooleanExtra("active", false);
                i.putExtra("packageName", "packageName");
                i.putExtra("title", "packageName");
                i.putExtra("text", "packageName");
                i.putStringArrayListExtra("profiles", profs);
                name = intent.getStringExtra("name");
                intent.putExtra("info", "schedule is " + active);

                context.sendBroadcast(i);
                new UpdateSchedule(name, active).execute();
                break;
            case ADD_PROFILE:
                i = new Intent(INSERT_NOTIFICATION);
                i.putExtra("packageName", "packageName");
                i.putExtra("title", "packageName");
                i.putExtra("text", "packageName");
                i.putStringArrayListExtra("profiles", profs);
                name = intent.getStringExtra("name");
                intent.putExtra("info", "profile is active");
                context.sendBroadcast(i);

                new UpdateProfile(intent.getStringExtra("name"), true).execute();
                break;
            case REMOVE_PROFILE:
                name = intent.getStringExtra("name");
                new UpdateProfile(name, false).execute();
//                new ChangePrevNotifications(intent.getStringArrayListExtra("profiles")).execute();

                break;
            case CHANGE_NOTIFICATIONS:
                new ChangePrevNotifications(intent.getStringArrayListExtra("profiles")).execute();
                break;

            case INSERT_NOTIFICATION:
                String packageName, title, text;
                packageName = intent.getStringExtra("packageName");
                title = intent.getStringExtra("title");
                text = intent.getStringExtra("text");
                profiles = intent.getStringArrayListExtra("profiles");
                new InsertNotification(packageName, title, text, profiles).execute();
                break;
        }
//        String temp = intent.getExtras().getString("info")+ "\n----------------------------------------------" + txtView.getText();
//        txtView.setText(temp+"");
    }

    private class UpdateProfile extends AsyncTask<Void, Void, Void> {
        private String profileName;
        private boolean active;

        public UpdateProfile(String profileName, boolean active){
            super();
            this.profileName = profileName;
            this.active = active;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ProfileEntity profile = db.profileDao().loadProfileSync(profileName);
            if(profile != null){
                profile.setActive(active);
                db.profileDao().update(profile);
            }

            return null;
        }
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
            }
            return null;
        }
    }

    private class ChangePrevNotifications extends AsyncTask<Void, Void, Void> {
        ArrayList<String> profiles;

        public ChangePrevNotifications(ArrayList<String> profiles){
            super();
            this.profiles = profiles;
        }

        @Override
        protected Void doInBackground(Void... params) {
            db.prevNotificationDao().deleteAll();
            for(String profile: profiles) {
                List<MinNotificationEntity> minNotifs = db.minNotificationDao().loadMinNotificationsFromProfileSync(profile);
                for (MinNotificationEntity notif : minNotifs) {
                    MinNotification mn = new MinNotification(notif.getAppName(), notif.getNotificationContext(), notif.getDate(), notif.getProfileName());
                    db.prevNotificationDao().insert(new PrevNotificationEntity(mn));
                    db.minNotificationDao().delete(notif);
                }
            }

            return null;
        }
    }

    private class UpdateSchedule extends AsyncTask<Void, Void, Void> {
        private String scheduleName;
        private boolean active;
        private String intentAction;

        public UpdateSchedule(String scheduleName, boolean active){
            super();
            this.scheduleName = scheduleName;
            this.active = active;
            if(active)
                intentAction = NLService.ADD_PROFILE;
            else
                intentAction = NLService.REMOVE_PROFILE;

        }

        @Override
        protected Void doInBackground(Void... params) {
            ScheduleEntity schedule = db.scheduleDao().loadScheduleSync(scheduleName);
            schedule.setActive(active);
            db.scheduleDao().update(schedule);

            for(String profile: schedule.getProfiles()){
                ProfileEntity profileEntity = db.profileDao().loadProfileSync(profile);
                Intent i = new Intent(intentAction);
                i.putExtra("name", profile);
                i.putExtra("appsToBlock", profileEntity.getAppsToBlock());
                i.putExtra("active", active);
                context.sendBroadcast(i);

//                profileEntity.setActive(true);
//                db.profileDao().update(profileEntity);
            }
            if(!active)
                new ChangePrevNotifications(schedule.getProfiles()).execute();

            return null;
        }
    }
}