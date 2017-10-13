package com.pk.example;

import java.util.Date;

public class Schedule {

    public String name;

    public String[] profiles;

    public Date[] startTimes;

    public int duration; // duration in minutes

    public boolean repeatWeekly;

    public Schedule() {

    }

    public Schedule(String name, String[] profiles, Date[] startTimes, int duration, boolean repeatWeekly){
        this.name = name;
        this.profiles = profiles;
        this.startTimes = startTimes;
        this.duration = duration;
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


    public int getDuration() {
        return duration;
    }

    public boolean getRepeatWeekly() {
        return repeatWeekly;
    }

}
