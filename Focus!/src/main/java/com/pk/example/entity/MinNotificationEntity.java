package com.pk.example.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


import com.pk.example.MinNotification;

import java.util.Date;

@Entity(tableName = "min_notifications")
public class MinNotificationEntity extends MinNotification {

    @PrimaryKey(autoGenerate = true)
    private int _id;
    private String _appName;
    private String _notificationContext;
    private String _appIcon;
    private Date _date;

    @Override
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    @Override
    public String getAppName() {
        return _appName;
    }

    public void setAppName(String appName) {
        this._appName = appName;
    }

    @Override
    public String getNotificationContext() {
        return _notificationContext;
    }

    public void setNotificationContext(String notificationContext) {
        this._notificationContext = notificationContext;
    }

    @Override
    public String getAppIcon() {
        return _appIcon;
    }

    public void setAppIcon(String appIcon) {
        this._appIcon = appIcon;
    }

    @Override
    public Date getDate() {
        return _date;
    }

    public void setDate(Date date) {
        this._date = date;
    }

    public MinNotificationEntity() {
    }

    public MinNotificationEntity(MinNotification minNotification) {
        this._appName = minNotification.getAppName();
        this._notificationContext = minNotification.getNotificationContext();
        this._appIcon = minNotification.getAppIcon();
        this._date = minNotification.getDate();
    }
}