package com.pk.example.clientui;

import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.pk.example.R;
import com.pk.example.database.AppDatabase;
import com.pk.example.entity.ProfileEntity;
import com.pk.example.entity.ScheduleEntity;
import com.pk.example.servicereceiver.DateManipulator;
import com.pk.example.servicereceiver.ProfileScheduler;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.app.TimePickerDialog;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScheduleViewActivity extends ListActivity{
    private String flag;
//    private ScheduleEntity scheduleEntity; // for edit/delete
    private List<ProfileEntity> profileList;
    EditText txtDate, txtTime, txtDuration, txtDay;
    private Integer chosenYear, chosenMonth, chosenDay, chosenHour, chosenMinute, durationHours, durationMins;
    ListAdapter listadaptor = null;
    private AppDatabase db;
    TextView textView;
    private AppDatabase database;
//    private String name;
    private int id;
//    ScheduleEntity scheduleInsert;
    final String[] daysOfWeek = new String[]{"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
    final String[] shortDaysOfWeek = new String[]{"S", "M", "T", "W", "TH", "F", "SA"};
    // view
    TextView tvScheduleName, tvStartTimes, tvDuration, tvRepeatWeekly, tvDaysOfWeek;

    boolean[] checkedDays = new boolean[]{
            false, // S
            false, // M
            false, // T
            false, // W
            false, // Th
            false, // F
            false // Ss
    };
//    boolean repeatWeekly = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag = String.valueOf(getIntent().getStringExtra("flag"));
        database = AppDatabase.getDatabase(getApplicationContext());

        // create profile mode
        if (flag.equals("create")) {
            setContentView(R.layout.activity_schedule_create);
        }
        else{
            id = getIntent().getIntExtra("id", -1);

        }
        // schedule edit mode
        if (flag.equals("edit")) {
            setContentView(R.layout.activity_schedule_edit);
            new GetEditScheduleInfo().execute();
            new LoadSelectedProfiles().execute();
        }


        if (flag.equals("view")) {
            setContentView(R.layout.activity_schedule_view);
            new GetViewScheduleInfo().execute();
        }
        else{
            new LoadProfiles().execute();

            //common to both edit and create
//            btnDatePicker=(Button)findViewById(R.id.btn_date);
//            btnTimePicker=(Button)findViewById(R.id.btn_time);
//            btnDayPicker=(Button)findViewById(R.id.btn_day);
            txtDate=(EditText)findViewById(R.id.in_date);
            txtTime=(EditText)findViewById(R.id.in_time);
            txtDuration=(EditText)findViewById(R.id.in_duration);
            txtDay = (EditText)findViewById(R.id.in_day);
            textView=(TextView)findViewById(R.id.textView);
            txtDate.setEnabled(false);
            txtDuration.setEnabled(false);
            txtDay.setEnabled(false);
            txtTime.setEnabled(false);

        }

        ListView listView = getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


    }

    public void showDurationPicker(View v){
        DurationPickerDialog timePickerDialog = new DurationPickerDialog(this,
                new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        txtDuration.setText(timeFormat(hourOfDay, minute));
                        durationHours = hourOfDay;
                        durationMins = minute;
                    }
                }, 0, 0);
        timePickerDialog.show();
    }

    public void showDatePicker(View v){
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        chosenDay = dayOfMonth;
                        chosenMonth = monthOfYear;
                        chosenYear = year;
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void showTimePicker(View v){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        txtTime.setText(timeFormat(hourOfDay, minute));
                        chosenHour = hourOfDay;
                        chosenMinute = minute;
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    public void showDayOfWeekPicker(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleViewActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        final boolean[] selectedItem = {false, false, false, false, false, false, false};
        final ArrayList selectedItems = new ArrayList();

        int temp = -1;
        if(checkedDays != null){
            for(int a = 0 ; a < 7; a++){
                selectedItem[a] = checkedDays[a];
                if(checkedDays[a])
                    selectedItems.add(a);
            }
        }
        else if (chosenDay != null)
        {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, chosenDay);
            c.set(Calendar.MONTH, chosenMonth);
            c.set(Calendar.YEAR, chosenYear);

            int day = c.get(Calendar.DAY_OF_WEEK) ;
            selectedItem[day-1] = true;
            temp = day - 1;
            selectedItems.add(day-1);
        }

        final int disablePosition = temp;

        builder.setTitle("Select Repeat Days");
        builder.setMultiChoiceItems(daysOfWeek, selectedItem, new DialogInterface.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (which == disablePosition)
                {
                    ((AlertDialog) dialog).getListView().setItemChecked(which, true);
                    ((AlertDialog) dialog).getListView().getChildAt(which).setEnabled(false);
                } else if (isChecked){
                    selectedItems.add(which);
                } else if (selectedItems.contains(which)){
                    selectedItems.remove(Integer.valueOf(which));
                }
            }
        })
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                txtDay.setText("");
                String put = "";
                checkedDays =  selectedItem;
                for (int i = 0; i < selectedItems.size(); i++)
                {
                    checkedDays[(Integer)selectedItems.get(i)] = true;
                }
                for (int i = 0; i < checkedDays.length; i++)
                {
                    if (checkedDays[i])
                    {
                        put += shortDaysOfWeek[i] + " ";
                    }
                }
                put = put.substring(0,put.length()-1);
                txtDay.setText(put);
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                txtDay.setText("");
            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }

    public ArrayList<Date> getStartTimes(){
        ArrayList<Date> startTimes = new ArrayList<Date>();
        Calendar dateChosen = DateManipulator.getCalendarFromChosenTime(chosenDay, chosenMonth, chosenYear, chosenHour, chosenMinute);
        for (int i = 0; i < checkedDays.length; i++) {
            if (checkedDays[i]) {
                dateChosen.set(Calendar.DAY_OF_WEEK, i+1);
                if (Calendar.getInstance().getTime().getDay() > i)
                {
                    dateChosen.add(Calendar.DATE, 7);
                }

                Date d = dateChosen.getTime();
                if(!startTimes.contains(d))
                    startTimes.add(d);
            }
        }
        if(startTimes.size() == 0)
            startTimes.add(dateChosen.getTime());
        return startTimes;
    }

    public ArrayList<String> getProfiles(){
        ArrayList<String> profiles;
        if(profileList.size() == 0){
            profiles = new ArrayList<String>();
        }
        else{
            profiles = listadaptor.getSelectedApps();
        }
        return profiles;
    }

    public boolean getRepeatWeekly(){
        Boolean repeatWeekly = false;
        for (int i = 0; i < checkedDays.length; i++)
        {
            if (checkedDays[i])
            {
                repeatWeekly = true;
            }

        }
        return repeatWeekly;
    }

    public void createButtonClicked(View v){
        // get new profile name from text field
        EditText nameText = (EditText) findViewById(R.id.editTextScheduleName);
        String scheduleName = nameText.getText().toString();

        if(allFieldsSet(scheduleName)) {
            ArrayList<Date> startTimes = getStartTimes();
            ArrayList<String> profiles = getProfiles();
            boolean repeatWeekly = getRepeatWeekly();

            ScheduleEntity scheduleInsert = new ScheduleEntity(new Schedule(scheduleName, profiles, startTimes, durationHours, durationMins, repeatWeekly, false));
            new InsertSchedule(scheduleInsert).execute();

            // notify user
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Schedule created", Toast.LENGTH_SHORT);
            toast.show();

            // TODO return to ProfileListActivity
            Intent i = new Intent(this, ScheduleListActivity.class);
            startActivity(i);
        }
    }

    public boolean allFieldsSet(String scheduleName){
        if (scheduleName.isEmpty()) {
            // show toast if profile name is empty
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Please enter a schedule name", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        else if (chosenHour == null || chosenYear == null || durationHours == null) {
            // show toast if no apps are selected
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Please choose a date, time, and duration", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        //REMOVED FOR TESTING -- UNCOMMMENT LATER
        else if (durationHours >= 10 || (durationHours == 0 && durationMins < 10)){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Duration must be between 10 minutes and 10 hours", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }


    public void editButtonClicked(View v) {
        Intent i = new Intent(getApplicationContext(), ScheduleViewActivity.class);
        i.putExtra("flag", "edit");
        i.putExtra("id", id);
        startActivity(i);
    }

    public void saveButtonClicked(View v) {
        // update schedule
        // get new profile name from text field
        EditText nameText = (EditText) findViewById(R.id.editTextScheduleName);
        String scheduleName = nameText.getText().toString();

        if(allFieldsSet(scheduleName)) {
            ArrayList<Date> startTimes = getStartTimes();
            ArrayList<String> profiles = getProfiles();
            boolean repeatWeekly = getRepeatWeekly();

            ScheduleEntity updatedSchedule = new ScheduleEntity(new Schedule(scheduleName, profiles, startTimes, durationHours, durationMins, repeatWeekly, false));
            updatedSchedule.setId(id);
            new UpdateSchedule(updatedSchedule).execute();

            // notify user
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Schedule updated", Toast.LENGTH_SHORT);
            toast.show();

            Intent i = new Intent(getApplicationContext(), ScheduleListActivity.class);
            startActivity(i);
        }
    }

    public void deleteButtonClicked(View v) {

        new DeleteSchedule().execute();

        // notify user
        Toast toast = Toast.makeText(getApplicationContext(),
                "Schedule deleted", Toast.LENGTH_SHORT);
        toast.show();

        Intent i = new Intent(getApplicationContext(), ScheduleListActivity.class);
        startActivity(i);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        getListView().setItemChecked(position, listadaptor.changeCheckedState(position));
    }

    private class LoadProfiles extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            db = AppDatabase.getDatabase(getApplicationContext());
            profileList = db.profileDao().loadAllProfilesAsync();
            if (profileList.size()==0) {
                profileList.add(new ProfileEntity(new Profile("No profiles to add.", new ArrayList<>( Arrays.asList("Buenos Aires", "Córdoba", "La Plata")), false)));
//                return null;
            }
            listadaptor = new ListAdapter(ScheduleViewActivity.this,
                    R.layout.profile_list_row, profileList);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(listadaptor);
            super.onPostExecute(result);
        }
    }

    private class LoadSelectedProfiles extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            db = AppDatabase.getDatabase(getApplicationContext());
            ScheduleEntity scheduleEntity = db.scheduleDao().loadScheduleSync(id);

            profileList = db.profileDao().loadAllProfilesAsync();

            ArrayList<ProfileEntity> plist = new ArrayList<>();

            for (ProfileEntity pe : profileList) {
                if (scheduleEntity.getProfiles().contains(pe.getName())) {
                    plist.add(pe);
                }
            }

            if (plist.size()==0) {
                plist.add(new ProfileEntity(new Profile("No profiles to show.", new ArrayList<>( Arrays.asList("Buenos Aires", "Córdoba", "La Plata")), false)));
            }

            listadaptor = new ListAdapter(ScheduleViewActivity.this,
                    R.layout.profile_list_row, plist);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(listadaptor);
            super.onPostExecute(result);
        }
    }

    private class InsertSchedule extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;
        private ScheduleEntity created;

        InsertSchedule(ScheduleEntity created) {
            this.created = created;
        }
        @Override
        protected Void doInBackground(Void... params) {
            database.scheduleDao().insert(created);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ScheduleViewActivity.this, null,
                    "Saving");
            super.onPreExecute();
        }
    }

    private class UpdateSchedule extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;
        private ScheduleEntity updated;

        UpdateSchedule(ScheduleEntity updated) {
            this.updated = updated;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ScheduleEntity original = database.scheduleDao().loadScheduleSync(id);
            updated.setActive(original.getActive());
            database.scheduleDao().update(updated);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ScheduleViewActivity.this, null,
                    "Saving");
            super.onPreExecute();
        }

    }

    private class DeleteSchedule extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            ScheduleEntity schedule = db.scheduleDao().loadScheduleSync(id);
            ProfileScheduler.disableSchedule(getApplicationContext(), schedule);
            db.scheduleDao().delete(schedule);
            return null;
        }
    }

    private class GetEditScheduleInfo extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            ScheduleEntity schedule = database.scheduleDao().loadScheduleSync(id);
            setEditScheduleInfo(schedule);
            return null;
        }
    }

    private class GetViewScheduleInfo extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            ScheduleEntity schedule = database.scheduleDao().loadScheduleSync(id);
            setViewScheduleInfo(schedule);
            return null;
        }
    }

    public void setViewScheduleInfo(ScheduleEntity schedule) {
        tvScheduleName = (TextView) findViewById(R.id.textViewScheduleName);
        tvStartTimes = (TextView) findViewById(R.id.textViewStartTimes);
        tvDuration = (TextView) findViewById(R.id.textViewDuration);
        tvRepeatWeekly = (TextView) findViewById(R.id.textViewRepeatWeekly);
        tvDaysOfWeek = (TextView) findViewById(R.id.textViewDaysOfWeek);

        tvScheduleName.setText(schedule.getName());

        ArrayList<Date> dates = schedule.getStartTimes();
//        if(dates.isEmpty()) {
//            int here = 0;
//        }

        Date date = dates.get(0);
        int durationHour = schedule.getDurationHr();
        int durationMinute = schedule.getDurationMin();
        tvStartTimes.setText("Start time: " + hourMinFormat(date));
        tvDuration.setText("Duration: " + timeFormat(durationHour, durationMinute));

        if (schedule.getRepeatWeekly()) {
            tvRepeatWeekly.setText("Repeated weekly: yes");
            boolean[] daysOfWeek = DateManipulator.getDaysOfWeekFromStartTimes(schedule.getStartTimes());
            tvDaysOfWeek.setText("Days of Week: " + getDaysOfWeekString(daysOfWeek));
        } else {
            tvRepeatWeekly.setText("Repeated weekly: no");
            tvDaysOfWeek.setText("Date: " + dateFormat(date));
        }

    }

    public void setEditScheduleInfo(ScheduleEntity schedule) {
        //populate schedule's info
        tvScheduleName = (TextView) findViewById(R.id.editTextScheduleName);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);
        txtDuration=(EditText)findViewById(R.id.in_duration);
        txtDay = (EditText)findViewById(R.id.in_day);
        tvScheduleName.setText(schedule.getName());


        ArrayList<Date> dates = schedule.getStartTimes();
//
//        if(dates.isEmpty()) {
//            int here = 0;
//        }
        //TODO FIX THIS TO HAVE MULTIPLE DATES
        Date date = dates.get(0);
        Calendar c = DateManipulator.getCalendarFromDate(schedule.getStartTimes().get(0));
        chosenDay = c.get(Calendar.DAY_OF_MONTH);
        chosenMonth = c.get(Calendar.MONTH);
        chosenYear = c.get(Calendar.YEAR);
        chosenHour = c.get(Calendar.HOUR_OF_DAY);
        chosenMinute = c.get(Calendar.MINUTE);
        durationHours = schedule.getDurationHr();
        durationMins  = schedule.getDurationMin();

        boolean[] daysOfWeek = DateManipulator.getDaysOfWeekFromStartTimes(schedule.getStartTimes());
        checkedDays =daysOfWeek;
        txtDate.setText(dateFormat(date));
        txtTime.setText(hourMinFormat(date));
        txtDuration.setText(timeFormat(durationHours, durationMins));
        txtDay.setText(getDaysOfWeekString(daysOfWeek));




    }

    public String getDaysOfWeekString(boolean[] daysOfWeek){
//        boolean a = daysOfWeek[0];
        String days = "";
        for(int i = 0; i < 7; i++){
            if(daysOfWeek[i])
                days += shortDaysOfWeek[i] + " ";
        }
        return days;
    }

    public String[] getDaysOfWeek() {
        return daysOfWeek;
    }

    public String[] getShortDaysOfWeek() {
        return shortDaysOfWeek;
    }

    public String timeFormat(int hours, int min){
        return formatNumber(hours) + ":" + formatNumber(min);
    }

    public String dateFormat(Date d){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); //Or whatever format fits best your needs.
        return sdf.format(d);
    }

    public String hourMinFormat(Date d){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); //Or whatever format fits best your needs.
        return sdf.format(d);
    }

    private String formatNumber(int number) {
        String result = "";
        if (number < 10) {
            result += "0";
        }
        result += number;

        return result;
    }


}
