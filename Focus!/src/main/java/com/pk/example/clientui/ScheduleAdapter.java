package com.pk.example.clientui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.pk.example.R;
import com.pk.example.database.AppDatabase;
import com.pk.example.entity.ProfileEntity;
import com.pk.example.entity.ScheduleEntity;
import com.pk.example.servicereceiver.DateManipulator;
import com.pk.example.servicereceiver.ProfileScheduler;

public class ScheduleAdapter extends ArrayAdapter<ScheduleEntity> {
    private List<ScheduleEntity> scheduleEntities = null;
    private Context context;
    SwitchCompat switchCompat;
    ToggleButton b;
    private AppDatabase database;
    private static final String FORMAT = "%02d:%02d:%02d";


    public ScheduleAdapter(Context context, int textViewResourceId,
                               List<ScheduleEntity> scheduleEntities) {
        super(context, textViewResourceId, scheduleEntities);
        this.context = context;
        this.scheduleEntities = scheduleEntities;
        database = AppDatabase.getDatabase(context);

    }

    @Override
    public int getCount() {
        return ((null != scheduleEntities) ? scheduleEntities.size() : 0);
    }

    @Override
    public ScheduleEntity getItem(int position) {
        return ((null != scheduleEntities) ? scheduleEntities.get(position) : null);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.schedule_list_row, null);
        }

        final ScheduleEntity schedule = scheduleEntities.get(position);


        if (null != schedule) {
            TextView profileContext = (TextView) view.findViewById(R.id.name);
            profileContext.setText(schedule.getName());
            b = (ToggleButton) view.findViewById(R.id.toggBtn);
            final TextView countdownTimer = (TextView) view.findViewById(R.id.countdown);
            if(schedule.getName().equals("There are no schedules to display.")){
                ((ViewGroup) b.getParent()).removeView(b);

            }
            else {
                if(schedule.getIsEnabled()){
                    b.setChecked(true);

                    if(schedule.getActive() && schedule.shouldBeActive()) {
                        Date startDate = new Date();
                        Date endDate = DateManipulator.getEndDate(startDate, schedule.getDurationHr(), schedule.getDurationMin());
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm"); //Or whatever format fits best your needs.
                        String test = sdf.format(startDate);
                        test = sdf.format(endDate);
                        long difference = DateManipulator.getTimeDiffMillis(startDate, endDate);
                        new CountDownTimer(difference, 1000) { // adjust the milli seconds here

                            public void onTick(long millisUntilFinished) {

                                countdownTimer.setText("ACTIVE " + String.format(FORMAT,
                                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                            }

                            public void onFinish() {

                            }
                        }.start();
                    }
                }
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int durationSec = schedule.getDurationHr() * 3600 + schedule.getDurationMin() * 60;
//                        CountDownTimer countdown = new CountDownTimer(durationSec * 1000, 1000) { // adjust the milli seconds here
//
//                            public void onTick(long millisUntilFinished) {
//
//                                countdownTimer.setText("ACTIVE "+String.format(FORMAT,
//                                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
//                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
//                                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
//                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
//                                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
//                            }
//
//                            public void onFinish() {
//
//                            }
//                        };
                        if (b.isChecked()) {
//                            new EnableSchedule(getItem(position)).execute();
                            ProfileScheduler.enableSchedule(context, schedule);
                            schedule.setIsEnabled(true);
                            if(schedule.shouldBeActive()){

                            }
                            //countdown.start();
                        } else {
                            ProfileScheduler.disableSchedule(context, schedule);
                            schedule.setIsEnabled(false);
//                            countdown.cancel();
                            countdownTimer.setText("");
                        }
                        new UpdateSchedule(schedule).execute();

                    }
                });

            }

        }
        return view;
    }

    private class UpdateSchedule extends AsyncTask<Void, Void, Void> {
        private ScheduleEntity schedule;

        public UpdateSchedule(ScheduleEntity schedule ){
            super();
            this.schedule = schedule;
        }

        @Override
        protected Void doInBackground(Void... params) {
            database.scheduleDao().update(schedule);
            return null;
        }
    }

//    private class EnableSchedule extends AsyncTask<Void, Void, Void> {
//        private ScheduleEntity schedule;
//
//        public EnableSchedule(ScheduleEntity schedule ){
//            super();
//            this.schedule = schedule;
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            ArrayList<String> profiles = schedule.getProfiles();
//            for(String profile: profiles){
//                ProfileEntity prof = database.profileDao().loadProfileSync(profile);
//                ProfileScheduler.enableSchedule(context, schedule, profile, prof.getAppsToBlock());
//            }
//            return null;
//        }
//    }

}
