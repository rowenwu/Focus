package com.pk.example;


import java.util.ArrayList;
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
        ArrayList<String> appsToBlock = new ArrayList<>();
        appsToBlock.add("com.facebook.orca");
        Profile profile = new Profile(fakeyname, appsToBlock, true);
        return profile;
    }

    // fake
    public static Schedule makeFakeSchedule(int minsFromNow){
        ArrayList<String> profiles = new ArrayList<>();
        profiles.add("ProfileName");

        Calendar cal = Calendar.getInstance();
        long t= cal.getTimeInMillis();
        Date date =new Date(t + (minsFromNow * ONE_MINUTE_IN_MILLIS));
        ArrayList<Date> startTimes = new ArrayList<>();
        startTimes.add(date);
        return new Schedule("ScheduleName", profiles, startTimes, 0, 120, true);
    }

}