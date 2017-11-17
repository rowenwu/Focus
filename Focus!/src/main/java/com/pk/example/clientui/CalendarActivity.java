package com.pk.example.clientui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.pk.example.R;
import com.pk.example.database.AppDatabase;
import com.pk.example.entity.PrevNotificationEntity;
import com.pk.example.entity.ScheduleEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by williamxu on 11/4/17.
 */

public class CalendarActivity extends Activity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;
    private AppDatabase database;
    private List<ScheduleEntity> scheduleEntityList;
    private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LoadSchedules().execute();

        setContentView(R.layout.activity_week_view);

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);
    }


    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Intent i = new Intent(this, ScheduleViewActivity.class);
        i.putExtra("flag", "edit");
        i.putExtra("name", event.getName());
        startActivity(i);
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        onEventClick(event, eventRect);
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {

    }

    public WeekView getWeekView() {
        return mWeekView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar_menu, menu);
        return true;
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        getWeekView().notifyDatasetChanged();
        getWeekView().invalidate();

        //list of schedules for the month/year
        List<WeekViewEvent> matchedEvents = new ArrayList<>();
        //get the ones that match
        for(WeekViewEvent event : events) {
            if(eventMatches(event, newYear, newMonth - 1)) {
                matchedEvents.add(event);
            }
        }

        return matchedEvents;
    }

    private boolean eventMatches(WeekViewEvent event, int year, int month) {

        int eYear = event.getStartTime().get(Calendar.YEAR);
        int eMonth = event.getStartTime().get(Calendar.MONTH);
        return ( eYear == year &&  eMonth == month)
                || (event.getEndTime().get(Calendar.YEAR)== year && event.getEndTime().get(Calendar.MONTH) == month);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_today:
                mWeekView.goToToday();
                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;

        }

        return super.onOptionsItemSelected(item);
    }



    //get schedules from database
    private class LoadSchedules extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... params) {
            //get database
            database = AppDatabase.getDatabase(getApplicationContext());
            scheduleEntityList = database.scheduleDao().loadAllSchedulesSync();


            int id = 0;
            int currYear = 0;
            for(ScheduleEntity schedule : scheduleEntityList) {
                //get random color for schedule
                Random rand = new Random();
                int r = rand.nextInt(255);
                int g = rand.nextInt(255);
                int b = rand.nextInt(255);
                int randomColor = Color.rgb(r, g, b);

                //start times for schedule
                ArrayList<Date> daysOfSchedule = schedule.getStartTimes();
                for(Date date : daysOfSchedule) {
                    Calendar startTime = Calendar.getInstance();
                    startTime.set(date.getYear()+1900,date.getMonth(),date.getDate(),date.getHours(),date.getMinutes());

                    Calendar endTime = Calendar.getInstance();
                    endTime.set(date.getYear()+1900,date.getMonth(),date.getDate(),date.getHours() + schedule.getDurationHr(),date.getMinutes()+schedule.getDurationMin());
                    WeekViewEvent event = new WeekViewEvent(id, schedule.getName(), startTime, endTime);
                    event.setColor(randomColor);
                    events.add(event);
                    id += 1;
                    currYear = date.getYear()+1900;
                }
            }


            //add Holiday Events
            //New Year's Day
            Calendar startTime = Calendar.getInstance();
            Calendar endTime = Calendar.getInstance();
            String holidayName = "New Year's Day";
            startTime.set(currYear,0,0,0,0);
            endTime.set(currYear,0,0,1,0);
            WeekViewEvent event = new WeekViewEvent(id, holidayName, startTime, endTime);
            events.add(event);


            // check if Christmas
            String holidayName2 = "Christmas";
            Calendar startTime2 = Calendar.getInstance();
            Calendar endTime2 = Calendar.getInstance();
            startTime2.set(currYear,11,25,0,0);
            endTime2.set(currYear,11,25,1,0);
            event = new WeekViewEvent(id, holidayName2, startTime2, endTime2);
            events.add(event);

            // check if 4th of July
            String holidayName3 = "4th Of July";
            Calendar startTime3 = Calendar.getInstance();
            Calendar endTime3 = Calendar.getInstance();
            startTime3.set(currYear,6,3,0,0);
            endTime3.set(currYear,6,3,1,0);
            event = new WeekViewEvent(id, holidayName3, startTime3, endTime3);
            events.add(event);

//
//  // check Thanksgiving (4th Thursday of November)
//            String holidayName4 = "Thanksgiving";
//            Calendar startTime4 = Calendar.getInstance();
//            Calendar endTime4 = Calendar.getInstance();
//            startTime4.set(currYear,11);
//            startTime4.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
//            startTime4.set(Calendar.DAY_OF_WEEK_IN_MONTH, 4);
//            startTime4.set(Calendar.HOUR_OF_DAY, 0);
//
//            endTime4.set(currYear,11);
//            endTime4.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
//            endTime4.set(Calendar.DAY_OF_WEEK_IN_MONTH, 4);
//            endTime4.set(Calendar.HOUR_OF_DAY, 1);
//            event = new WeekViewEvent(id, holidayName4, startTime4, endTime4);
//            events.add(event);
////            // check Memorial Day (last Monday of May)
////            if (month == Calendar.MAY
////                    && cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
////                    && cal.get(Calendar.DAY_OF_MONTH) > (31 - 7) ) {
////                return "Memorial Day";
////            }
//
//            // check Labor Day (1st Monday of September)
//            String holidayName5 = "Labor Day";
//            Calendar startTime5 = Calendar.getInstance();
//            Calendar endTime5 = Calendar.getInstance();
//            startTime5.set(currYear,8);
//            startTime5.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//            startTime5.set(Calendar.HOUR_OF_DAY, 0);
//            endTime5.set(currYear,8);
//            endTime5.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//            endTime5.set(Calendar.HOUR_OF_DAY, 1);
//            event = new WeekViewEvent(id, holidayName5, startTime5, endTime5);
//            events.add(event);

//            // check President's Day (3rd Monday of February)
//            String holidayName6 = "President's Day";
//            Calendar startTime6 = Calendar.getInstance();
//            Calendar endTime6 = Calendar.getInstance();
//            startTime6.set(currYear,1);
//            startTime6.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//            startTime6.set(Calendar.DAY_OF_WEEK_IN_MONTH, 3);
//            startTime6.set(Calendar.HOUR_OF_DAY, 0);
//
//            endTime6.set(currYear,1);
//            endTime6.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//            endTime6.set(Calendar.DAY_OF_WEEK_IN_MONTH, 3);
//            endTime6.set(Calendar.HOUR_OF_DAY, 1);
//            event = new WeekViewEvent(id, holidayName6, startTime6, endTime6);
//            events.add(event);
//
//            // check MLK Day (3rd Monday of January)
//            String holidayName7 = "MLK Day";
//            Calendar startTime7 = Calendar.getInstance();
//            Calendar endTime7 = Calendar.getInstance();
//            startTime7.set(currYear,0);
//            startTime7.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//            startTime7.set(Calendar.DAY_OF_WEEK_IN_MONTH, 3);
//            startTime7.set(Calendar.HOUR_OF_DAY, 0);
//
//            endTime7.set(currYear,0);
//            endTime7.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//            endTime7.set(Calendar.DAY_OF_WEEK_IN_MONTH, 3);
//            endTime7.set(Calendar.HOUR_OF_DAY, 1);
//            event = new WeekViewEvent(id, holidayName7, startTime7, endTime7);
//            events.add(event);

            return null;
        }
    }
}
