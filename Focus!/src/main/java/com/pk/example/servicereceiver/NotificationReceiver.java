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
        int id;
        this.context = context;

        switch(intent.getAction()) {
            case TOGGLE_SCHEDULE:
                id = intent.getIntExtra("id", -1);

                boolean active = intent.getBooleanExtra("active", false);
                new UpdateSchedule(id, active).execute();
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
        private int id;
        private boolean active;
        private String intentAction;

        public UpdateSchedule(int id, boolean active){
            super();
            this.id = id;
            this.active = active;
            if(active)
                intentAction = NLService.ADD_PROFILE;
            else
                intentAction = NLService.REMOVE_PROFILE;

        }

        @Override
        protected Void doInBackground(Void... params) {
            ScheduleEntity schedule = db.scheduleDao().loadScheduleSync(id);
            schedule.setActive(active);
            db.scheduleDao().update(schedule);


            //NOT SURE WHY THIS ISN'T HAPPENING??
            for(String profile: schedule.getProfiles()){
                ProfileEntity profileEntity = db.profileDao().loadProfileSync(profile);
                Intent i = new Intent(intentAction);
                i.putExtra("name", profile);
                i.putExtra("apps", profileEntity.getAppsToBlock());
                int j = profileEntity.getAppsToBlock().size();
//                i.putExtra("active", active);
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