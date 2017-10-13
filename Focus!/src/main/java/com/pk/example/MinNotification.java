package com.pk.example;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class MinNotification {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public String appName;

    public String notificationContext;

    public String appIcon;

    public Date date;

    public MinNotification() {

    }

    public int getId() {
        return id;
    }

    public String getAppName() {
        return appName;
    }

    public String getNotificationContext() {
        return notificationContext;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public Date getDate() {
        return date;
    }

}
