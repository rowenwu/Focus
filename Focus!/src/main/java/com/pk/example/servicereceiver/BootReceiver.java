package com.pk.example.servicereceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.pk.example.database.AppDatabase;
import com.pk.example.entity.ProfileEntity;
import com.pk.example.entity.ScheduleEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by kathe on 11/3/2017.
 */

public class BootReceiver extends BroadcastReceiver {
    private AppDatabase db;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        db = AppDatabase.getDatabase(context);
        context = context;

        CreateHolidayAlarms();

        new CheckForActiveSchedules().execute();
    }

    //create alarms for every holiday
    private void CreateHolidayAlarms() {

        //New Years
        Calendar newYearsCal = Calendar.getInstance();
        newYearsCal.set(Calendar.MONTH, 0);
        newYearsCal.set(Calendar.DAY_OF_MONTH, 1);
        Intent hasPendingIntentNewYears;
        hasPendingIntentNewYears = new Intent(NLService.HOLIDAY_SCHEDULE);
        hasPendingIntentNewYears.putExtra("start", true);
        //get calendar
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(newYearsCal.getTime());
        hasPendingIntentNewYears.putExtra("calendar", formatted);
        PendingIntent newYearsIntent = createPendingIntent(context, 200, NLService.HOLIDAY_SCHEDULE, (int) newYearsCal.getTimeInMillis());
        setAlarm(context, newYearsCal, newYearsIntent);
        context.sendBroadcast(hasPendingIntentNewYears);

        //test
        Calendar testCal = Calendar.getInstance();
        testCal.set(Calendar.MONTH, 10);
        testCal.set(Calendar.DAY_OF_MONTH, 27);
        Intent hasPendingIntentTest;
        hasPendingIntentTest = new Intent(NLService.HOLIDAY_SCHEDULE);
        hasPendingIntentTest.putExtra("start", true);
        //get calendar
        String formattedTest = format1.format(testCal.getTime());
        hasPendingIntentTest.putExtra("calendar", formattedTest);
        PendingIntent testIntent = createPendingIntent(context, 200, NLService.HOLIDAY_SCHEDULE, (int) testCal.getTimeInMillis());
        setAlarm(context, testCal, testIntent);
        context.sendBroadcast(hasPendingIntentTest);

    }

    // NEED TO CREATE DIFFERENT PENDING INTENT IDS AND STORE THEM IN NLSERVICE
    public static PendingIntent createPendingIntent(Context context, int id, String intentAction, int alarmID) {
        //create alarms - pendingintents
        Intent i = new Intent(intentAction);
        i.putExtra("id", id);
//        context.sendBroadcast(i);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, (int) alarmID, i, 0);

        return alarmIntent;
    }

    public static void setAlarm(Context context, Calendar calendar, PendingIntent alarmIntent){
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(ALARM_SERVICE);

//        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
//        calendar.setTime(date);
//        calendar.add(Calendar.HOUR_OF_DAY, addHr);
//        calendar.add(Calendar.MINUTE, addMin);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }

    public static void removeAlarm(Context context, PendingIntent pi) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        alarmMgr = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        if (alarmMgr!= null) {
            alarmMgr.cancel(pi);
        }
    }



//    // check if New Year's Day
//        if (month == 0
//            && dayOfMonth == 1) {
//        return " New Year's Day";
//    }
//
//    // check if Christmas
//        if (month == 11
//            && dayOfMonth == 25) {
//        return "Christmas";
//    }
//
//    // check if 4th of July
//        if (month == 3
//            && dayOfMonth == 4) {
//        return "4th of July";
//    }
//
//    // check Thanksgiving (4th Thursday of November)
//        if (month == 10
//            && dayOfWeekInMonth == 4
//            && cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
//        return "Thanksgiving";
//    }
//
//    // check Memorial Day (last Monday of May)
//        if (month == Calendar.MAY
//                && cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
//                && cal.get(Calendar.DAY_OF_MONTH) > (31 - 7) ) {
//        return "Memorial Day";
//    }
//
//    // check Labor Day (1st Monday of September)
//        if (month == 8
//            && dayOfWeekInMonth == 1
//            && cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
//        return "Labor Day";
//    }
//
//    // check President's Day (3rd Monday of February)
//        if (month == 1
//            && dayOfWeekInMonth == 3
//            && cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
//        return "President's Day";
//    }
//
////        // check Veterans Day (November 11)
////        if (month == Calendar.NOVEMBER
////                && dayOfMonth == 11) {
////            return true;
////        }
//
//    // check MLK Day (3rd Monday of January)
//        if (month == 0
//            && dayOfWeekInMonth == 3
//            && cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
//        return "MLK Day";
//    }




    private class CheckForActiveSchedules extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            List<ScheduleEntity> enabledSchedules = db.scheduleDao().loadEnabledSchedulesSync(true);
            for(ScheduleEntity schedule: enabledSchedules){
                //check if any schedules that are enabled should be active but have not been set to active
                if(schedule.getActive()) {
                    if(!schedule.shouldBeActive()){
                        //replace with toggle_schedule intent??/
                        schedule.setActive(false);
                        db.scheduleDao().update(schedule);
                        for(String profile: schedule.getProfiles()){
                            ProfileEntity profileEntity = db.profileDao().loadProfileSync(profile);
                            Intent i = new Intent(NLService.REMOVE_PROFILE);
                            i.putExtra("name", profile);
                            i.putExtra("appsToBlock", profileEntity.getAppsToBlock());
                            context.sendBroadcast(i);

//                profileEntity.setActive(true);
//                db.profileDao().update(profileEntity);
                        }
                    }
                }
                //check if any schedules that are active shouldn't be active (are not enabled, current time isn't within range of start/end
                else{
                    if(schedule.shouldBeActive()){
                        //replace with toggle_schedule intent??/
                        schedule.setActive(true);
                        db.scheduleDao().update(schedule);
                        for(String profile: schedule.getProfiles()){
                            ProfileEntity profileEntity = db.profileDao().loadProfileSync(profile);
                            Intent i = new Intent(NLService.ADD_PROFILE);
                            i.putExtra("name", profile);
                            i.putExtra("appsToBlock", profileEntity.getAppsToBlock());
                            context.sendBroadcast(i);

                        }
                    }
                }

            }

            return null;
        }

        boolean isWithinRange(Date startTime, Date endTime) {
            Date currentTime = new Date();
            return !(currentTime.before(startTime) || currentTime.after(endTime));
        }
    }

}
