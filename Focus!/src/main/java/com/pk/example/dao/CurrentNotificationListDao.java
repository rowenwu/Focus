package com.pk.example.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;


import com.pk.example.MinNotification;
import com.pk.example.entity.CurrentNotificationListEntity;
import com.pk.example.entity.MinNotificationEntity;
import com.pk.example.entity.ScheduleEntity;

import java.util.ArrayList;
import java.util.List;


@Dao
public interface CurrentNotificationListDao {
    @Query("SELECT * FROM curr_notifications")
    LiveData<List<MinNotificationEntity>> loadAllCurrNotifications();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MinNotificationEntity> currNotifications);

    @Query("select * from curr_notifications where id = :currNotificationID")
    LiveData<CurrentNotificationListEntity> loadCurrNotification(int currNotificationID);

    @Query("select * from curr_notifications where id = :currNotificationID")
    CurrentNotificationListEntity loadCurrvNotificationSync(int currNotificationID);
}