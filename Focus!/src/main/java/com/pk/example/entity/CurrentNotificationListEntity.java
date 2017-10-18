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
    private int _listId;

    @Embedded
    private MinNotificationEntity _notification;

    public CurrentNotificationListEntity() {
    }

    public int getListId(){
        return _listId;
    }

    public void setListId(int id) {
        this._listId = id;
    }

    public MinNotificationEntity getNotification(){
        return _notification;
    }

    public void setNotification(MinNotificationEntity notification) {
        this._notification = notification;
    }
}