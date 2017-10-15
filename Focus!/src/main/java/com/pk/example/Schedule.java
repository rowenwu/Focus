package com.pk.example;

import java.util.ArrayList;
import java.util.Date;

public class Schedule {

    private String name;

    private ArrayList<String> profiles;

    private ArrayList<Date> startTimes;

    private int durationHr; // number of hours

    private int durationMin; //number of minutes

    private boolean repeatWeekly;

    public Schedule() {

    }

    public Schedule(String name, ArrayList<String> profiles, ArrayList<Date> startTimes, int durationHr, int durationMin, boolean repeatWeekly){
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

    public ArrayList<String> getProfiles() {
        return profiles;
    }


    public ArrayList<Date> getStartTimes() {
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
