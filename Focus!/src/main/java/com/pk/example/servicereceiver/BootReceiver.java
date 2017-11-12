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

        new CheckForActiveSchedules().execute();
    }
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