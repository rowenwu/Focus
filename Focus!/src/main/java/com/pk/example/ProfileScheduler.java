package com.pk.example;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;

public class ProfileScheduler {

    private static AlarmManager alarmMgr;

    public static void turnOnSchedule(Context context, Schedule schedule) {
        //get schedule time and profiles from database

        Date[] startTimes = schedule.startTimes;
        for (int j = 0; j < schedule.profiles.length; j++)
            for (int i = 0; i < startTimes.length; i++) {
                createStartProfileAlarm(context, schedule.profiles[j], startTimes[i], schedule.repeatWeekly);
                //create end profile alarm
            }
    }

    // NEED TO CREATE DIFFERENT PENDING INTENT IDS AND STORE THEM IN NLSERVICE
    public static void createStartProfileAlarm(Context context, String profile, Date date, boolean repeat) {
        //create alarms - pendingintents and pass to nlservice

        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(NLService.ADD_PROFILE);
        i.putExtra("profile", profile);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), i, 0);

        // send the intent to NLService in case we need to cancel it later
        Intent hasPendingIntent;
        hasPendingIntent = new Intent(Intent.ACTION_SENDTO);
        hasPendingIntent.putExtra("profile", profile);
        hasPendingIntent.putExtra("pendingIntent", alarmIntent);
        context.sendBroadcast(hasPendingIntent);

        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);

        if (repeat)
            //creates a weekly repeating alarm
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmMgr.INTERVAL_DAY * 7, alarmIntent);
        else
            alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }

    public static void turnOffSchedule(Context context, Schedule schedule) {
        //get schedule time and profiles from database

        Date[] startTimes = schedule.startTimes;
        for (int j = 0; j < schedule.profiles.length; j++)
            for (int i = 0; i < startTimes.length; i++){
                // send intent to NLService to invoke removeAlarm on all the pending intents
            }
//                removeCreateProfileAlarm(context, schedule.profiles[j]);

    }

    public static void removeAlarm(Context context, PendingIntent pi) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmMgr!= null) {
            alarmMgr.cancel(pi);
        }
    }
}