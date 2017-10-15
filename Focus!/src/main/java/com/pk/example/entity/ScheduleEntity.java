package com.pk.example.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.pk.example.Schedule;

import java.util.Date;

@Entity
public class ScheduleEntity extends Schedule{

    @PrimaryKey
    public String name;

    public String[] profiles;

    public Date[] startTimes;

    public int durationHr; // number of hours

    public int durationMin; //number of minutes

    public boolean repeatWeekly;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String[] getProfiles() {
        return profiles;
    }

    public void setProfiles(String[] profiles) {
        this.profiles = profiles;
    }

    @Override
    public Date[] getStartTimes() {
        return startTimes;
    }

    public void setStartTimes(Date[] startTimes) {
        this.startTimes = startTimes;
    }

    @Override
    public int getDurationHr() {
        return durationHr;
    }

    @Override
    public int getDurationMin(){
        return durationMin;
    }

    public void setDurationHr(int durationHr) {
        this.durationHr = durationHr;
    }

    public void setDurationMin(int setDurationMin) {
        this.durationHr = setDurationMin;
    }

    @Override
    public boolean getRepeatWeekly() {
        return repeatWeekly;
    }

    public void setRepeatWeekly(boolean repeatWeekly) {
        this.repeatWeekly = repeatWeekly;
    }

    public ScheduleEntity(){
    }

    public ScheduleEntity(Schedule schedule) {
        this.name = schedule.getName();
        this.profiles = schedule.getProfiles();
        this.startTimes = schedule.getStartTimes();
        this.durationHr = schedule.getDurationHr();
        this.durationMin = schedule.getDurationMin();
        this.repeatWeekly = schedule.getRepeatWeekly();
    }

}
