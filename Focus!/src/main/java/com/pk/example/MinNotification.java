package com.pk.example;

import android.graphics.Bitmap;

import java.util.Date;

public class MinNotification {

//    private int id;

    public String appName;

    public String notificationContext;

    //public Bitmap appIcon;

    public Date date;

    public String profileName;

    public MinNotification() {

    }

    public MinNotification(String appName, String notificationContext, Date date, String profileName) {
        this.appName = appName;
        this.notificationContext = notificationContext;
        this.date = date;
        this.profileName = profileName;
    }

//    public int getId() {
//        return id;
//    }

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
    public String getProfileName() {
        return profileName;
    }
}