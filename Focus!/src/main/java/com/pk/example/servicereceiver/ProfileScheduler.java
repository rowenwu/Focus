package com.pk.example.servicereceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.pk.example.entity.ProfileEntity;
import com.pk.example.entity.ScheduleEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;

import static android.content.Context.ALARM_SERVICE;

public class ProfileScheduler {

    public static void enableSchedule(Context context, ScheduleEntity schedule) {
        ArrayList<Date> startTimes = schedule.getStartTimes();

        for (int i = 0; i < startTimes.size(); i++) {
//            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm"); //Or whatever format fits best your needs.
//            String date = sdf.format(startTimes.get(i));
            Intent hasPendingIntent;
            hasPendingIntent = new Intent(NLService.ADD_SCHEDULE_PENDING_INTENT);
            hasPendingIntent.putExtra("name", schedule.getName());
            //create intent to start profile
            PendingIntent startIntent = createPendingIntent(context, schedule.getName(), NLService.TOGGLE_SCHEDULE, true, ((int)Calendar.getInstance().getTimeInMillis())+i);
            hasPendingIntent.putExtra("startIntent", startIntent);
            setAlarm(context, startTimes.get(i), 0, 0, schedule.getRepeatWeekly(), startIntent);

            //create intent to end profile
            PendingIntent endIntent = createPendingIntent(context, schedule.getName(), NLService.TOGGLE_SCHEDULE, false, ((int)Calendar.getInstance().getTimeInMillis())+(i*2));
            setAlarm(context, startTimes.get(i), schedule.getDurationHr(), schedule.getDurationMin(), schedule.getRepeatWeekly(), endIntent);
            hasPendingIntent.putExtra("endIntent", endIntent);
            context.sendBroadcast(hasPendingIntent);
        }
    }


    // NEED TO CREATE DIFFERENT PENDING INTENT IDS AND STORE THEM IN NLSERVICE
    public static PendingIntent createPendingIntent(Context context, String profile, ArrayList<String> appsToBlock, String intentAction, int alarmID) {
        //create alarms - pendingintents
        Intent i = new Intent(intentAction);
        i.putExtra("name", profile);
        i.putExtra("appsToBlock", appsToBlock);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, (int) alarmID, i, 0);

        return alarmIntent;
    }

    // NEED TO CREATE DIFFERENT PENDING INTENT IDS AND STORE THEM IN NLSERVICE
    public static PendingIntent createPendingIntent(Context context, String schedule, String intentAction, boolean active, int alarmID) {
        //create alarms - pendingintents
        Intent i = new Intent(intentAction);
        i.putExtra("name", schedule);
        i.putExtra("active", active);
//        context.sendBroadcast(i);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, (int) alarmID, i, 0);

        return alarmIntent;
    }

    public static void setAlarm(Context context, Date date, int addHr, int addMin,  boolean repeat, PendingIntent alarmIntent){
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, addHr);
        calendar.add(Calendar.MINUTE, addMin);
        if (repeat)
            //creates a weekly repeating alarm
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmMgr.INTERVAL_DAY * 7, alarmIntent);
        else
            alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }

    // disable schedule
    public static void disableSchedule(Context context, ScheduleEntity schedule) {
        //get schedule time and profiles from database

        // TODO if schedule is active: remove profiles

        Intent hasPendingIntent;
        hasPendingIntent = new Intent(NLService.CANCEL_ALARM_INTENTS);
        hasPendingIntent.putExtra("name", schedule.getName());
        context.sendBroadcast(hasPendingIntent);

        if(schedule.getActive()){
            Intent i;
            i = new Intent(NLService.UPDATE_SCHEDULE_ACTIVE);
            i.putExtra("name", schedule.getName());
            i.putExtra("active", false);
            context.sendBroadcast(i);
        }

    }

    public static void removeAlarm(Context context, PendingIntent pi) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        alarmMgr = (AlarmManager) context.getSystemService(ALARM_SERVICE);

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
        PendingIntent endIntent = createPendingIntent(context, profile.getName(), profile.getAppsToBlock(), NLService.REMOVE_PROFILE, (int) Calendar.getInstance().getTimeInMillis());
        setAlarm(context, new Date(), 10, 0, false, endIntent);
        hasPendingIntent.putExtra("pendingIntent", endIntent);

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