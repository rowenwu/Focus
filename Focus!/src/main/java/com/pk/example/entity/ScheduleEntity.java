package com.pk.example.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.pk.example.clientui.Schedule;

import java.util.ArrayList;
import java.util.Date;

@Entity(tableName = "schedules")
public class ScheduleEntity extends Schedule {

    @PrimaryKey(autoGenerate = true)
    private int _id;
    private String _name;
    private ArrayList<String> _profiles;
    private ArrayList<Date> _startTimes;
    private int _durationHr; // number of hours
    private int _durationMin; //number of minutes
    private boolean _repeatWeekly;
    private boolean _isEnabled;
    private boolean _active;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public ArrayList<String> getProfiles() {
        return _profiles;
    }

    public void setProfiles(ArrayList<String> profiles) {
        this._profiles = profiles;
    }

    @Override
    public ArrayList<Date> getStartTimes() {
        return _startTimes;
    }

    public void setStartTimes(ArrayList<Date> startTimes) {
        this._startTimes = startTimes;
    }

    @Override
    public int getDurationHr() {
        return _durationHr;
    }

    public void setDurationHr(int durationHr) {
        this._durationHr = durationHr;
    }

    @Override
    public int getDurationMin(){
        return _durationMin;
    }

    public void setDurationMin(int durationMin) {
        this._durationMin = durationMin;
    }

    @Override
    public boolean getRepeatWeekly() {
        return _repeatWeekly;
    }

    public void setRepeatWeekly(boolean repeatWeekly) {
        this._repeatWeekly = repeatWeekly;
    }

    @Override
    public boolean getIsEnabled(){
        return _isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this._isEnabled = isEnabled;
    }

    public boolean getActive() {return _active;}

    public void setActive(boolean active) { this._active = active;}

    public ScheduleEntity () {

    }

    public ScheduleEntity (Schedule schedule){
        this._name = schedule.getName();
        this._profiles = schedule.getProfiles();
        this._startTimes = schedule.getStartTimes();
        this._durationHr = schedule.getDurationHr();
        this._durationMin = schedule.getDurationMin();
        this._repeatWeekly = schedule.getRepeatWeekly();
        this._isEnabled = schedule.getIsEnabled();
        this._active = false;
    }
}