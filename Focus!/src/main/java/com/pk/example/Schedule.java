package com.pk.example;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

public class Schedule {
    @PrimaryKey
//    int id;
    public String name;
    public String[] profiles;
    public Date[] startTimes;
    public int duration; // duration in minutes
    public boolean repeatWeekly;

    public Schedule(String name, String[] profiles, Date[] startTimes, int duration, boolean repeatWeekly){
        this.name = name;
        this.profiles = profiles;
        this.startTimes = startTimes;
        this.duration = duration;
        this.repeatWeekly = repeatWeekly;
    }

}
