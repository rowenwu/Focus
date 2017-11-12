package com.pk.example.servicereceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kathe on 11/3/2017.
 */

public class DateManipulator {

    public static Date getEndDate(Date startDate, int addHr, int addMin){
        return getEndCalendar(startDate, addHr, addMin).getTime();
    }


    public static Calendar getEndCalendar(Date startDate, int addHr, int addMin) {
        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
        calendar.setTime(startDate);
        calendar.add(Calendar.HOUR_OF_DAY, addHr);
        calendar.add(Calendar.MINUTE, addMin);
        return calendar;
    }

    public static Calendar getCalendarFromDate(Date date){
        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);
        return calendar;
    }

    public static int getDayOfWeek(Date date){
        return getCalendarFromDate(date).get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static Date getStartDateToday(Date date){
        Calendar calendar = getCalendarFromDate(date);
        int day = getCalendarFromDate(date).get(Calendar.DAY_OF_WEEK);
        calendar.set(Calendar.DAY_OF_WEEK, day);
        return calendar.getTime();
    }

    public static Calendar getCalendarFromChosenTime(int day, int month, int year, int hours, int min){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.HOUR_OF_DAY, hours);
        c.set(Calendar.MINUTE, min);
        return c;
    }

    public static boolean[] getDaysOfWeekFromStartTimes(ArrayList<Date> startTimes){
        boolean[] daysOfWeek = new boolean[7];
        for(Date d: startTimes){
            daysOfWeek[DateManipulator.getDayOfWeek(d)] = true;
        }
        return daysOfWeek;
    }

}
