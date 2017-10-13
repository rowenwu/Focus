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

    public int duration; // duration in minutes

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
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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
        this.duration = schedule.getDuration();
        this.repeatWeekly = schedule.getRepeatWeekly();
    }

}
