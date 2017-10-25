package com.pk.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.pk.example.entity.MinNotificationEntity;
import com.pk.example.entity.PrevNotificationEntity;
import com.pk.example.entity.ProfileEntity;
import com.pk.example.entity.ScheduleEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pk.example.NLService.ADD_PROFILE;
import static com.pk.example.NLService.CHANGE_NOTIFICATIONS;
import static com.pk.example.NLService.INSERT_NOTIFICATION;
import static com.pk.example.NLService.REMOVE_PROFILE;

/**
 * Created by kathe on 10/21/2017.
 */

public class NotificationReceiver extends BroadcastReceiver {
    private AppDatabase db;

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.i("intent ","intent "+intent.getExtras().toString());
        db = AppDatabase.getDatabase(context);
        ArrayList<String> profiles;

        switch(intent.getAction()) {
            case ADD_PROFILE:
//                ArrayList<String> profs = new ArrayList<String>();
//                profs.add("")
//                new InsertNotification("test", "test", "test", profs).execute();
                new UpdateProfile(intent.getStringExtra("name"), true).execute();
                break;
            case REMOVE_PROFILE:
                String name = intent.getStringExtra("name");
                new UpdateProfile(name, false).execute();
                break;
            case CHANGE_NOTIFICATIONS:
                new ChangePrevNotifications(intent.getStringArrayListExtra("profiles")).execute();
                break;
//            case UPDATE_SCHEDULE_ACTIVE:
//                new UpdateProfile(intent.getBooleanExtra("active")).execute();
//                break;
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
            profile.setActive(active);
            db.profileDao().update(profile);
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

        public UpdateSchedule(String scheduleName, boolean active){
            super();
            this.scheduleName = scheduleName;
            this.active = active;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ScheduleEntity schedule = db.scheduleDao().loadScheduleSync(scheduleName);
            schedule.setActive(active);
            db.scheduleDao().update(schedule);
            return null;
        }
    }
}