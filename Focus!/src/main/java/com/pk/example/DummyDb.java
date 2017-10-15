package com.pk.example;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DummyDb {
    static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs

    public static Schedule[] getAllSchedules(){
        // create array with one fake schedule, set two mins from now
        Schedule[] schedules = new Schedule[1];
        schedules[0] = makeFakeSchedule(2);
        return schedules;
    }

    public static Profile getProfile(String name){
        return makeFakeProfile(name);
    }


    //fake
    public static Profile makeFakeProfile(String fakeyname){
        String[] appsToBlock = {"com.facebook.orca"};
        Profile profile = new Profile(fakeyname, appsToBlock);
        return profile;
    }

    // fake
    public static Schedule makeFakeSchedule(int minsFromNow){
        String[] profiles = {"ProfileName"};

        Calendar cal = Calendar.getInstance();
        long t= cal.getTimeInMillis();
        Date date =new Date(t + (minsFromNow * ONE_MINUTE_IN_MILLIS));
        Date[] startTimes = new Date[1];
        startTimes[0] = date;
        return new Schedule("ScheduleName", profiles, startTimes, 0, 2, true);
    }

}
