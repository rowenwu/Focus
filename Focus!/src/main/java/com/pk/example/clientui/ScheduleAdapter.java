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

import org.w3c.dom.Text;

public class ScheduleAdapter extends ArrayAdapter<ScheduleEntity> {
    private List<ScheduleEntity> scheduleEntities = null;
    private Context context;
    SwitchCompat switchCompat;
    ToggleButton b;
    private AppDatabase database;
    private static final String FORMAT = "%02d:%02d:%02d";
    private CountDownTimer[] timers;


    public ScheduleAdapter(Context context, int textViewResourceId,
                               List<ScheduleEntity> scheduleEntities) {
        super(context, textViewResourceId, scheduleEntities);
        this.context = context;
        this.scheduleEntities = scheduleEntities;
        database = AppDatabase.getDatabase(context);

        timers = new CountDownTimer[scheduleEntities.size()];
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
            } else {
                //check if current date is holiday and disable
                Calendar currentDate = Calendar.getInstance();
                ArrayList<Date> startTimes = schedule.getStartTimes();
                for(int i = 0; i < startTimes.size(); i++) {
                    //current date
                    Calendar scheduleDate = Calendar.getInstance();
                    scheduleDate.setTime(startTimes.get(i));
                    Holidays holiday = new Holidays();
                    Calendar calendar = Calendar.getInstance();
                    //get date from schedule list
                    Date date = startTimes.get(i);
                    calendar.setTime(date);
                    final String holy = holiday.checkIfHoliday(calendar);
                    if (scheduleDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
                            scheduleDate.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH) &&
                            scheduleDate.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH)) {

                        if (holy != "") {
                            if (schedule.getIsEnabled()) {
//                            new EnableSchedule(getItem(position)).execute();
                                ProfileScheduler.enableSchedule(context, schedule);
                                schedule.setIsEnabled(true);
                                if (schedule.getIsEnabled()) {
                                    timers[position] = setCountDownTimer(countdownTimer, schedule);
                                }
                            } else {
                                b.setChecked(false);
                                countdownTimer.setText(holy);
                                ProfileScheduler.disableSchedule(context, schedule);
                            }
                        }
                    }
                    if (schedule.getIsEnabled()) {
                        b.setChecked(true);

                        if (schedule.getActive() && schedule.shouldBeActive()) {
                            timers[position] = setCountDownTimer(countdownTimer, schedule);
                        }
                    }
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (b.isChecked()) {
//                            new EnableSchedule(getItem(position)).execute();
                                ProfileScheduler.enableSchedule(context, schedule);
                                schedule.setIsEnabled(true);
                                if (schedule.shouldBeActive()) {
                                    timers[position] = setCountDownTimer(countdownTimer, schedule);
                                }
                                //countdown.start();
                            } else {
                                ProfileScheduler.disableSchedule(context, schedule);
                                schedule.setIsEnabled(false);
                                if (timers[position] != null)
                                    timers[position].cancel();
                                if (holy != "") {
                                    countdownTimer.setText(holy);
                                } else {
                                    countdownTimer.setText("");
                                }

                            }
                            new UpdateSchedule(schedule).execute();
                        }
                    });
                }
            }
        }
        return view;
    }

    public CountDownTimer setCountDownTimer(final TextView txtView, ScheduleEntity schedule){
        Date startDate = new Date();
        Date endDate = DateManipulator.getEndDate(schedule.getStartTimes().get(0), schedule.getDurationHr(), schedule.getDurationMin());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm"); //Or whatever format fits best your needs.
        String test = sdf.format(startDate);
        test = sdf.format(endDate);
        long difference = DateManipulator.getTimeDiffMillis(startDate, endDate);
        CountDownTimer countdown = new CountDownTimer(difference, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                txtView.setText("ACTIVE " + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {

            }
        }.start();
        return countdown;
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
