package com.pk.example.clientui;

import com.pk.example.servicereceiver.DateManipulator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Schedule {

    private String name;

    private ArrayList<String> profiles;

    private ArrayList<Date> startTimes;

    private int durationHr; // number of hours

    private int durationMin; //number of minutes

    private boolean repeatWeekly;

    private boolean isEnabled;

    public Schedule() {

    }

    public Schedule(String name, ArrayList<String> profiles, ArrayList<Date> startTimes, int durationHr, int durationMin, boolean repeatWeekly, boolean isEnabled){
        this.name = name;
        this.profiles = profiles;
        this.startTimes = startTimes;
        this.durationHr = durationHr;
        this.durationMin = durationMin;
        this.repeatWeekly = repeatWeekly;
        this.isEnabled = isEnabled;
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

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public Boolean[] getDaysOfWeek(){
        Boolean[] daysOfWeek = new Boolean[8];
        for(Date d: startTimes){
            daysOfWeek[DateManipulator.getDayOfWeek(d)] = true;
        }
        return daysOfWeek;
    }


}