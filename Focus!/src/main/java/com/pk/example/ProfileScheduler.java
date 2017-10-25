package com.pk.example;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.pk.example.entity.ProfileEntity;
import com.pk.example.entity.ScheduleEntity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ProfileScheduler {

    private static AlarmManager alarmMgr;

    //enable schedule to become active later
    public static void enableSchedule(Context context, ScheduleEntity schedule) {
        //get schedule time and profiles from database

        ArrayList<String> profiles = schedule.getProfiles();
        ArrayList<Date> startTimes = schedule.getStartTimes();
        for (int j = 0; j < profiles.size(); j++)
            for (int i = 0; i < startTimes.size(); i++) {
                // send the intent to NLService in case we need to cancel it later
                Intent hasPendingIntent;
                hasPendingIntent = new Intent(NLService.ADD_SCHEDULE_PENDING_INTENT);
                hasPendingIntent.putExtra("name", schedule.getName());

                //TODO ADD APPS TO BLOCK TO INTENT
                hasPendingIntent.putExtra("startIntent",
                        createAlarm(context, profiles.get(j), startTimes.get(i), 0, 0, schedule.getRepeatWeekly(), NLService.ADD_PROFILE));
                hasPendingIntent.putExtra("endIntent",
                        createAlarm(context, profiles.get(j), startTimes.get(i), schedule.getDurationHr(), schedule.getDurationMin(), schedule.getRepeatWeekly(), NLService.REMOVE_PROFILE));
                context.sendBroadcast(hasPendingIntent);
            }
    }

    // NEED TO CREATE DIFFERENT PENDING INTENT IDS AND STORE THEM IN NLSERVICE
    public static PendingIntent createAlarm(Context context, String profile, Date date, int addHr, int addMin, boolean repeat, String intentAction) {
        //create alarms - pendingintents

        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(intentAction);
        i.putExtra("name", profile);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), i, 0);
        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, addHr);
        calendar.add(Calendar.MINUTE, addMin);

        if (repeat)
            //creates a weekly repeating alarm
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmMgr.INTERVAL_DAY * 7, alarmIntent);
        else
            alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

        return alarmIntent;
    }

    // disable schedule
    public static void disableSchedule(Context context, Schedule schedule) {
        //get schedule time and profiles from database

        // TODO if schedule is active: remove profiles

        Intent hasPendingIntent;
        hasPendingIntent = new Intent(NLService.CANCEL_ALARM_INTENTS);
        hasPendingIntent.putExtra("name", schedule.getName());
        context.sendBroadcast(hasPendingIntent);

    }

    public static void removeAlarm(Context context, PendingIntent pi) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmMgr!= null) {
            alarmMgr.cancel(pi);
        }
    }

    //randomly turn on a profile - will either be on for 10 hours or whenever a user turns it off
    public static void turnOnProfile(Context context, ProfileEntity profile){
        Intent i;
        i = new Intent(NLService.ADD_PROFILE);
        i.putExtra("name", profile.getName());
        i.putStringArrayListExtra("apps", profile.getAppsToBlock());
        context.sendBroadcast(i);

        Intent hasPendingIntent;
        hasPendingIntent = new Intent(NLService.ADD_PROFILE_PENDING_INTENT);
        hasPendingIntent.putExtra("name", profile.getName());

        // add profile alarm intent to nlservice
        hasPendingIntent.putExtra("pendingIntent", createAlarm(context, profile.getName(), new Date(), 10, 0, false, NLService.REMOVE_PROFILE));

        //TODO sned change notifications alarm intent to notificationreceiver
    }

    //randomly turn off a profile
    public static void turnOffProfile(Context context, ProfileEntity profile){
        //SEND A INTENT TO NOTIFICATION RECEIVER TO CHANGE PREV NOTIFICATIONS

        // send intent to cancel NLService end profile pendingintent
        Intent i;
        i = new Intent(NLService.REMOVE_PROFILE);
        i.putExtra("name", profile.getName());
        i.putStringArrayListExtra("apps", profile.getAppsToBlock());
        context.sendBroadcast(i);


        Intent notificationIntent;
        notificationIntent = new Intent(NLService.CHANGE_NOTIFICATIONS);
        ArrayList<String> profiles = new ArrayList<String>();
        profiles.add(profile.getName());
        notificationIntent.putExtra("profiles", profiles);
        context.sendBroadcast(notificationIntent);
    }


}