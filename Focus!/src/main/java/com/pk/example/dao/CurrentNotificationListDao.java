package com.pk.example.dao;

import android.app.Notification;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;


import com.pk.example.MinNotification;
import com.pk.example.entity.CurrentNotificationListEntity;
import com.pk.example.entity.ScheduleEntity;

import java.util.List;


@Dao
public interface CurrentNotificationListDao {
    @Query("SELECT * FROM curr_notifications")
    LiveData<List<CurrentNotificationListEntity>> loadAllCurrNotifications();

    //Insert multiple notifcation
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CurrentNotificationListEntity> currNotifications);

    @Query("select * from curr_notifications where id = :currNotificationID")
    LiveData<CurrentNotificationListEntity> loadCurrNotification(int currNotificationID);

    @Query("select * from curr_notifications where id = :currNotificationID")
    CurrentNotificationListEntity loadCurrvNotificationSync(int currNotificationID);

    //insert a new notification
    @Insert
    public void insert(CurrentNotificationListEntity currentNotificationListEntity);

    //delete all notifications
    @Query("DELETE FROM curr_notifications")
    public void deleteAll();
}