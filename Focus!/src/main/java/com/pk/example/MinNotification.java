package com.pk.example;

import android.graphics.Bitmap;

import java.util.Date;

public class MinNotification {

    private int id;

    public String appName;

    public String notificationContext;

    //public Bitmap appIcon;

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

   /* public Bitmap getAppIcon() {
        return appIcon;
    }
*/
    public Date getDate() {
        return date;
    }

}