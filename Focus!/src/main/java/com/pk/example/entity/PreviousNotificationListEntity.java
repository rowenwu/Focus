package com.pk.example.entity;

import com.pk.example.MinNotification;
import com.pk.example.Profile;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@Entity(tableName = "prev_notifications")
public class PreviousNotificationListEntity {

    @PrimaryKey
    private int listId;

    //@Embedded
    //private MinNotificationEntity notificationList;

    @Embedded
    private MinNotificationEntity notification;

    public PreviousNotificationListEntity() {
        //notificationList = new ArrayList<MinNotificationEntity>();
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int id) {
        this.listId = id;
    }

    /*
    public List<MinNotificationEntity> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<MinNotificationEntity> notificationList) {
        this.notificationList = notificationList;
    }

    public void addNotification(MinNotificationEntity minNotification) {
        notificationList.add(minNotification);
    }
*/
    public MinNotificationEntity getNotification() {
        return notification;
    }

    public void setNotification(MinNotificationEntity notification) {
        this.notification = notification;
    }

    public void addNotification(MinNotificationEntity minNotification) {
        this.notification = minNotification;
    }
}