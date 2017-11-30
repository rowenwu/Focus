package com.pk.example.servicereceiver;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import com.pk.example.database.AppDatabase;
import com.pk.example.entity.ProfileEntity;
import com.pk.example.entity.ScheduleEntity;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.Context.ALARM_SERVICE;
import static com.pk.example.servicereceiver.NLService.ADD_PROFILE;
import static com.pk.example.servicereceiver.NLService.REMOVE_PROFILE;
import static com.pk.example.servicereceiver.NLService.HOLIDAY_SCHEDULE;

/**
 * Created by kathe on 11/3/2017.
 */

public class ProfileScheduleReceiver  extends BroadcastReceiver {
    private AppDatabase db;
    private Context mContext;


    @Override
    public void onReceive(Context context, Intent intent) {
        db = AppDatabase.getDatabase(context);
        this.mContext = context;
        String name = intent.getStringExtra("name");
        ArrayList<String> apps = intent.getStringArrayListExtra("apps");
        Intent i= new Intent(context, ForegroundDetectorService.class);
        i.putExtra("name", name);
        i.putExtra("apps", apps);
        //TODO CHANGE NAME TO ID



        switch(intent.getAction()) {
            case ADD_PROFILE:
                new UpdateProfile(name, true).execute();
                i.setAction(ADD_PROFILE);
                break;
            case REMOVE_PROFILE:
                new UpdateProfile(name, false).execute();
                i.setAction(REMOVE_PROFILE);
                break;
            case HOLIDAY_SCHEDULE:
                boolean start = intent.getBooleanExtra("start", true);
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                String date = intent.getStringExtra("calendar");
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(format1.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ArrayList<Integer> scheduleIDList = intent.getIntegerArrayListExtra("scheduleIDs");
                new HolidayScheduleUpdate(start, calendar, context, scheduleIDList);
                i.setAction( HOLIDAY_SCHEDULE);
                break;
        }

        context.startService(i);
    }


    private class UpdateProfile extends AsyncTask<Void, Void, Void> {
        private String name;
        private boolean active;

        public UpdateProfile(String name, boolean active){
            super();
            this.name = name;
            this.active = active;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ProfileEntity profile = db.profileDao().loadProfileSync(name);
            if(profile != null){
                profile.setActive(active);
                db.profileDao().update(profile);
            }

            return null;
        }
    }

    private class HolidayScheduleUpdate extends AsyncTask<Void, Void, Void> {
        private boolean start;
        private Calendar calendar;
        private Context context;
        private ArrayList<Integer> scheduleIDList;


        public HolidayScheduleUpdate(boolean start, Calendar calendar, Context context, ArrayList<Integer> scheduleIDList){
            super();
            this.start = start;
            this.calendar = calendar;
            this.context = context;
            this.scheduleIDList = scheduleIDList;
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<ScheduleEntity> scheduleEntityList = db.scheduleDao().loadAllSchedulesSync();
            if(start) {
                ArrayList<Integer> scheduleIDs = null;
                for(ScheduleEntity schedule : scheduleEntityList) {
                    schedule.setIsEnabled(false);
                    db.scheduleDao().update(schedule);
                    scheduleIDs.add(schedule.getId());
                }

                //end alarm
                calendar.add(Calendar.DATE, 1);
                Intent hasPendingIntent;
                hasPendingIntent = new Intent(NLService.HOLIDAY_SCHEDULE);
                PendingIntent intent = createPendingIntent(context, 200, NLService.HOLIDAY_SCHEDULE, false, (int) calendar.getTimeInMillis());
                hasPendingIntent.putExtra("scheduleIDs", scheduleIDs);
                setAlarm(context, calendar, intent);
                context.sendBroadcast(hasPendingIntent);
            } else {
                for(Integer id : scheduleIDList) {
                    ScheduleEntity schedule = db.scheduleDao().loadScheduleSync(id);
                    schedule.setIsEnabled(true);
                    db.scheduleDao().update(schedule);
                }
            }

            return null;
        }

        // NEED TO CREATE DIFFERENT PENDING INTENT IDS AND STORE THEM IN NLSERVICE
        public PendingIntent createPendingIntent(Context context, int id, String intentAction, boolean start, int alarmID) {
            //create alarms - pendingintents
            Intent i = new Intent(intentAction);
            i.putExtra("id", id);
            i.putExtra("start", start);
//        context.sendBroadcast(i);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, (int) alarmID, i, 0);

            return alarmIntent;
        }

        public void setAlarm(Context context, Calendar calendar, PendingIntent alarmIntent){
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(ALARM_SERVICE);

//        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
//        calendar.setTime(date);
//        calendar.add(Calendar.HOUR_OF_DAY, addHr);
//        calendar.add(Calendar.MINUTE, addMin);
            alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        }

        public void removeAlarm(Context context, PendingIntent pi) {
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(ALARM_SERVICE);

            alarmMgr = (AlarmManager) context.getSystemService(ALARM_SERVICE);

            if (alarmMgr!= null) {
                alarmMgr.cancel(pi);
            }
        }
    }
}
