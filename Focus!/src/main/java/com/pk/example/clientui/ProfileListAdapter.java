package com.pk.example.clientui;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.pk.example.R;
import com.pk.example.database.AppDatabase;
import com.pk.example.entity.ProfileEntity;
import com.pk.example.servicereceiver.ProfileScheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by williamxu on 10/16/17.
 */

public class ProfileListAdapter extends ArrayAdapter<ProfileEntity> {
    private List<ProfileEntity> profileList = null;
    private Context context;
    ToggleButton b;
    private AppDatabase database;
    private int durationHours, durationMins;
    EditText txtDuration;
    private static final String FORMAT = "%02d:%02d:%02d";


    public ProfileListAdapter(Context context, int textViewResourceId,
                              List<ProfileEntity> profileList) {
        super(context, textViewResourceId, profileList);
        this.context = context;
        this.profileList = profileList;
        database = AppDatabase.getDatabase(context);

    }

    @Override
    public int getCount() {
        return ((null != profileList) ? profileList.size() : 0);
    }

    @Override
    public ProfileEntity getItem(int position) {
        return ((null != profileList) ? profileList.get(position) : null);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.schedule_list_row, null);
        }



        final ProfileEntity profileEntity = profileList.get(position);
        if (null != profileEntity) {
            TextView profileContext = (TextView) view.findViewById(R.id.name);
            profileContext.setText(profileEntity.getName());
            b = (ToggleButton) view.findViewById(R.id.toggBtn);
            final TextView countdownTimer = (TextView) view.findViewById(R.id.countdown);

            if(profileEntity.getName().equals("There are no profiles to display.")){
                ((ViewGroup) b.getParent()).removeView(b);

            }
            else {
                if(profileEntity.getActive()){
                    b.setChecked(true);

                    Date startDate = profileEntity.getEndTime();
                    long endHour = startDate.getHours();
                    long endMin = startDate.getMinutes();
                    long endSec = startDate.getSeconds();

                    Date currentDate = Calendar.getInstance().getTime();

                    long durrHour = endHour - currentDate.getHours();
                    long durrMin = endMin - currentDate.getMinutes();
                    long durrSec = endSec - currentDate.getSeconds();

                    countdownTimer.setText(endHour + " " + endMin + " " + currentDate.getHours() + " " + currentDate.getMinutes());

                    long durationSec = durrHour * 3600 + durrMin * 60 + durrSec;

                    new CountDownTimer(durationSec * 1000, 1000) { // adjust the milli seconds here

                        public void onTick(long millisUntilFinished) {

                            countdownTimer.setText("ACTIVE "+String.format(FORMAT,
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
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (b.isChecked()) {
                            ProfileScheduler.turnOnProfile(context, getItem(position));
                            profileEntity.setActive(true);
//
                            DurationPickerDialog timePickerDialog = new DurationPickerDialog(getContext(),
                                    new TimePickerDialog.OnTimeSetListener(){

                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay,
                                                              int minute) {

                                            durationHours = hourOfDay;
                                            durationMins = minute;

                                            Calendar calendar = Calendar.getInstance();
                                            calendar.setTime(new Date());
                                            calendar.add(Calendar.HOUR, durationHours);
                                            calendar.add(Calendar.MINUTE,  durationMins);
                                            Date date = calendar.getTime();


                                            new AddEndTime(profileEntity, date).execute();
                                        }
                                    }, 0, 0);
                            timePickerDialog.show();

                        } else {
                            ProfileScheduler.turnOffProfile(context, getItem(position));
                            profileEntity.setActive(false);
                            countdownTimer.setText("");
                        }
//                        new UpdateProfile(profileEntity).execute();
                    }
                });
            }

        }
        return view;
    }

    private class UpdateProfile extends AsyncTask<Void, Void, Void> {
        private ProfileEntity profile;

        public UpdateProfile(ProfileEntity profile ){
            super();
            this.profile = profile;
        }

        @Override
        protected Void doInBackground(Void... params) {
            database.profileDao().update(profile);
            return null;
        }
    }

    private class AddEndTime extends AsyncTask<Void, Void, Void> {
        private ProfileEntity profile;

        public AddEndTime(ProfileEntity _profile, Date date){
            super();
            this.profile = _profile;
            profile.setEndTime(date);
        }

        @Override
        protected Void doInBackground(Void... params) {
            database.profileDao().update(profile);
            return null;
        }
    }
}
