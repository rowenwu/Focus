package com.pk.example.entity;

import com.pk.example.MinNotification;
import com.pk.example.Profile;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "curr_notifications")
public class CurrentNotificationListEntity {

    @PrimaryKey
    private int id;

    //    @Embedded
    private ArrayList<MinNotification> notificationList;

    public CurrentNotificationListEntity() {
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<MinNotification> getNotificationList(){
        return notificationList;
    }

    public void setNotificationList(ArrayList<MinNotification> notificationList) {
        this.notificationList = notificationList;
    }
}