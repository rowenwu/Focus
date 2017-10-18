package com.pk.example.dao;

import android.app.Notification;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.pk.example.MinNotification;
import com.pk.example.entity.CurrentNotificationListEntity;
import com.pk.example.entity.MinNotificationEntity;
import com.pk.example.entity.PreviousNotificationListEntity;
import com.pk.example.entity.ScheduleEntity;

import java.util.ArrayList;
import java.util.List;



@Dao
public interface CurrentNotificationListDao {
    @Query("SELECT * FROM curr_notifications")
    List<CurrentNotificationListEntity> loadAllPrevNotifications();

    //insert a new notification list entity
    @Insert
    void insert(CurrentNotificationListEntity currentNotificationListEntity);

    @Update
    void update(CurrentNotificationListEntity currentNotificationListEntity);

    //delete all notifications
    @Query("DELETE FROM curr_notifications")
    void deleteAll();
}