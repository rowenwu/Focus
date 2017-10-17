package com.pk.example.dao;

import android.app.Notification;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;


import com.pk.example.MinNotification;
import com.pk.example.entity.MinNotificationEntity;
import com.pk.example.entity.PreviousNotificationListEntity;
import com.pk.example.entity.ScheduleEntity;

import java.util.ArrayList;
import java.util.List;



@Dao
public interface PreviousNotificationListDao {
    @Query("SELECT * FROM prev_notifications")
    LiveData<List<MinNotificationEntity>> loadAllPrevNotifications();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MinNotificationEntity> prevNotifications);

    //insert a new notification
    @Insert
    public void insert(PreviousNotificationListEntity previousNotificationListEntity);

    //delete all notifications
    @Query("DELETE FROM curr_notifications")
    public void deleteAll();
}