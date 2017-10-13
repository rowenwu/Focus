package com.pk.example.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.pk.example.MinNotification;

import java.util.Date;

@Entity
public class MinNotificationEntity extends MinNotification {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public String appName;

    public String notificationContext;

    public String appIcon;

    public Date date;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String getNotificationContext() {
        return notificationContext;
    }

    public void setNotificationContext(String notificationContext) {
        this.notificationContext = notificationContext;
    }

    @Override
    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    @Override
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public MinNotificationEntity() {
    }

    public MinNotificationEntity(MinNotification minNotification) {
        this.appName = minNotification.getAppName();
        this.notificationContext = minNotification.getNotificationContext();
        this.appIcon = minNotification.getAppIcon();
        this.date = minNotification.getDate();
    }
}
