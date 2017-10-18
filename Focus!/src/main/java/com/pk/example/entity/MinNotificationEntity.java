package com.pk.example.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

import com.pk.example.MinNotification;

import java.util.Date;

/*
@Entity(tableName = "min_notifications", foreignKeys = @ForeignKey(entity = PreviousNotificationListEntity.class,
        parentColumns = "id",
        childColumns = "_id"))
*/
@Entity(tableName = "min_notifications")
public class MinNotificationEntity extends MinNotification {

    @PrimaryKey(autoGenerate = true)
    private int _id;
    private String _appName;
    private String _notificationContext;
    // private Bitmap _appIcon;
    private Date _date;
    private String _profileName;

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
    public String getProfileName() {
        return _profileName;
    }

    public void setProfileName(String profileName) {
        this._profileName = profileName;
    }


    @Override
    public String getNotificationContext() {
        return _notificationContext;
    }

    public void setNotificationContext(String notificationContext) {
        this._notificationContext = notificationContext;
    }
    /*
        @Override
        public Bitmap getAppIcon() {
            return _appIcon;
        }

        public void setAppIcon(Bitmap appIcon) {
            this._appIcon = appIcon;
        }
    */
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
        this._id = minNotification.getId();
        this._appName = minNotification.getAppName();
        this._notificationContext = minNotification.getNotificationContext();
        //this._appIcon = minNotification.getAppIcon();
        this._date = minNotification.getDate();
    }

}