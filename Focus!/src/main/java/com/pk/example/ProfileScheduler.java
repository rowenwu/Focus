package com.pk.example;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class ProfileScheduler {

    private static AlarmManager alarmMgr;

    public static void turnOnSchedule(Context context, String schedule){
        //get schedule time and profiles from database
        //create pendingintents and pass to nlservice

        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, NLService.SchedulingReceiver.class);
        Intent i = new Intent(NLService.ADD_PROFILE);
        i.putExtra("profile", "com.facebook.orca");
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, i, 0);

        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 20);

// setRepeating() lets you specify a precise custom interval--in this case,
// 20 minutes.
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 2, alarmIntent);
    }
}
