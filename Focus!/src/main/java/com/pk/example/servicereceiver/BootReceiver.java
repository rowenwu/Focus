package com.pk.example.servicereceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.pk.example.database.AppDatabase;
import com.pk.example.entity.ProfileEntity;
import com.pk.example.entity.ScheduleEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        StringBuilder sb = new StringBuilder();
        sb.append("Action: " + intent.getAction() + "\n");
        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
        String log = sb.toString();
        Toast.makeText(context, log, Toast.LENGTH_LONG).show();


    }
    private class CheckForActiveSchedules extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            List<ScheduleEntity> enabledSchedules = db.scheduleDao().loadEnabledSchedulesSync(true);
            for(ScheduleEntity schedule: enabledSchedules){
                ArrayList<Date> startTimes = schedule.getStartTimes();
                Date startTime = new Date();
                Date endTime;
                if (schedule.getRepeatWeekly()) {
                    boolean[] daysOfWeek = DateManipulator.getDaysOfWeekFromStartTimes(startTimes);
                    if(daysOfWeek[DateManipulator.getDayOfWeek(startTime)]){
                        startTime = DateManipulator.getStartDateToday(startTimes.get(0));
                    }
                }
                else{
                    startTime = startTimes.get(0);
                }
                endTime = DateManipulator.getEndDate(startTime, schedule.getDurationHr(), schedule.getDurationMin());
                //check if any schedules that are enabled should be active but have not been set to active
                if(schedule.getActive()) {
                    if(!isWithinRange(startTime, endTime)){
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
                    if(isWithinRange(startTime, endTime)){
                        //replace with toggle_schedule intent??/
                        schedule.setActive(true);
                        db.scheduleDao().update(schedule);
                        //NOT SURE WHY THIS ISN'T HAPPENING??
                        for(String profile: schedule.getProfiles()){
                            ProfileEntity profileEntity = db.profileDao().loadProfileSync(profile);
                            Intent i = new Intent(NLService.ADD_PROFILE);
                            i.putExtra("name", profile);
                            i.putExtra("appsToBlock", profileEntity.getAppsToBlock());
                            context.sendBroadcast(i);

//                profileEntity.setActive(true);
//                db.profileDao().update(profileEntity);
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