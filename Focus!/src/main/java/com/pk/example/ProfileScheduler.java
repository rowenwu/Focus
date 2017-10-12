package com.pk.example;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;

public class ProfileScheduler {

    private static AlarmManager alarmMgr;

    public static void turnOnSchedule(Context context, Schedule schedule){
        //get schedule time and profiles from database

        Date[] startTimes = schedule.startTimes;
        for(int i = 0; i < startTimes.length; i++)
            createStartProfileAlarm(context, "com.facebook.orca", startTimes[i]);

    }


    public static void createStartProfileAlarm(Context context, String profile, Date date){
        //create alarms - pendingintents and pass to nlservice

        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(NLService.ADD_PROFILE);
        i.putExtra("profile", profile);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, i, 0);


//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        //set alarm to go off 2 mins from now
//        calendar.set(Calendar.MINUTE, min);

// setRepeating() lets you specify a precise custom interval--in this case,
// 2 minutes.
//        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                1000 * 60 * 2, alarmIntent);

        Schedule schedule = DummyDb.makeFakeSchedule(2);

        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
//        calendar.setTime(date);
        calendar.setTime(schedule.startTimes[0]);
        alarmMgr.set(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), alarmIntent);
    }
}
