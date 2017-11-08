package com.pk.example.clientui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.usage.UsageEvents;
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

        new LoadSchedules().execute();

    }


    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

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
        //list of schedules for the month/year
        List<WeekViewEvent> matchedEvents = new ArrayList<WeekViewEvent>();
        //get the ones that match
        for(WeekViewEvent event : events) {
            if(eventMatches(event, newYear, newMonth - 1)) {
                matchedEvents.add(event);
            }
        }

        return matchedEvents;
    }

    private boolean eventMatches(WeekViewEvent event, int year, int month) {

        int eYear = event.getStartTime().get(Calendar.YEAR) + 1900;
        int eMonth = event.getStartTime().get(Calendar.MONTH);
        return ( eYear == year &&  eMonth == month)
                || (event.getEndTime().get(Calendar.YEAR)  + 1900 == year && event.getEndTime().get(Calendar.MONTH) == month);
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
            getWeekView().notifyDatasetChanged();

            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... params) {
            //get database
            database = AppDatabase.getDatabase(getApplicationContext());
            scheduleEntityList = database.scheduleDao().loadAllSchedulesSync();


            int id = 0;
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
//                    startTime.set(Calendar.HOUR_OF_DAY, date.getHours());
//                    startTime.set(Calendar.MINUTE, date.getMinutes());
//                    startTime.set(Calendar.MONTH, date.getMonth());
//                    startTime.set(Calendar.YEAR, date.getYear());
                    startTime.set(date.getYear(),date.getMonth(),date.getDate(),date.getHours(),date.getMinutes());

                    Calendar endTime = Calendar.getInstance();
//                    endTime.set(Calendar.HOUR_OF_DAY, date.getHours() + schedule.getDurationHr());
//                    endTime.set(Calendar.MINUTE, date.getMinutes() + schedule.getDurationMin());
                    endTime.set(date.getYear(),date.getMonth(),date.getDate(),date.getHours() + schedule.getDurationHr(),date.getMinutes()+schedule.getDurationMin());
                    WeekViewEvent event = new WeekViewEvent(id, schedule.getName(), startTime, endTime);
                    event.setColor(randomColor);
                    events.add(event);
                }
            }
            return null;
        }
    }
}





