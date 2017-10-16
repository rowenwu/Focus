package com.pk.example.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;


import com.pk.example.entity.PreviousNotificationListEntity;
import com.pk.example.entity.ScheduleEntity;

import java.util.List;



@Dao
public interface PreviousNotificationListDao {
    @Query("SELECT * FROM prev_notifications")
    LiveData<List<PreviousNotificationListEntity>> loadAllPrevNotifications();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PreviousNotificationListEntity> prevNotifications);

    @Query("select * from prev_notifications where id = :prevNotificationID")
    LiveData<PreviousNotificationListEntity> loadPrevNotification(int prevNotificationID);

    @Query("select * from prev_notifications where id = :prevNotificationID")
    PreviousNotificationListEntity loadPrevNotificationSync(int prevNotificationID);
}