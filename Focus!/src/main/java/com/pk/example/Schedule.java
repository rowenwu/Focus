package com.pk.example;

import java.util.Date;

public class Schedule {

    public String name;

    public String[] profiles;

    public Date[] startTimes;

    public int durationHr; // number of hours

    public int durationMin; //number of minutes

    public boolean repeatWeekly;

    public Schedule() {

    }

    public Schedule(String name, String[] profiles, Date[] startTimes, int durationHr, int durationMin, boolean repeatWeekly){
        this.name = name;
        this.profiles = profiles;
        this.startTimes = startTimes;
        this.durationHr = durationHr;
        this.durationMin = durationMin;
        this.repeatWeekly = repeatWeekly;
    }

    public String getName() {
        return name;
    }

    public String[] getProfiles() {
        return profiles;
    }


    public Date[] getStartTimes() {
        return startTimes;
    }


    public int getDurationHr() {
        return durationHr;
    }

    public int getDurationMin(){
        return durationMin;
    }

    public boolean getRepeatWeekly() {
        return repeatWeekly;
    }

}
