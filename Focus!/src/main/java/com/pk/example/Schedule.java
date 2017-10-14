package com.pk.example;

import java.util.ArrayList;
import java.util.Date;

public class Schedule {

    private String name;
    private ArrayList<String> profiles;
    private ArrayList<Date> startTimes;
    private int duration; // duration in minutes
    private boolean repeatWeekly;

    public Schedule() {

    }

    public Schedule(String name, ArrayList<String> profiles, ArrayList<Date> startTimes, int duration, boolean repeatWeekly){
        this.name = name;
        this.profiles = profiles;
        this.startTimes = startTimes;
        this.duration = duration;
        this.repeatWeekly = repeatWeekly;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getProfiles() {
        return profiles;
    }

    public ArrayList<Date> getStartTimes() {
        return startTimes;
    }

    public int getDuration() {
        return duration;
    }

    public boolean getRepeatWeekly() {
        return repeatWeekly;
    }

}
