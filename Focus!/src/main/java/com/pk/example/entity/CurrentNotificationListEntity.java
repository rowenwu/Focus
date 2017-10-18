package com.pk.example.entity;

import com.pk.example.MinNotification;
import com.pk.example.Profile;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "curr_notifications")
public class CurrentNotificationListEntity {

    @PrimaryKey
    private int id;

    //    @Embedded
    private List<MinNotificationEntity> notificationList;

    public CurrentNotificationListEntity() {
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MinNotificationEntity> getNotificationList(){
        return notificationList;
    }

    public void setNotificationList(List<MinNotificationEntity> notificationList) {
        this.notificationList = notificationList;
    }
}