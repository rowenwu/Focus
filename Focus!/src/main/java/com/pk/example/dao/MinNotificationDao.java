package com.pk.example.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pk.example.entity.MinNotificationEntity;
import com.pk.example.entity.ProfileEntity;

import java.util.List;

@Dao
public interface MinNotificationDao {
//    @Query("SELECT * FROM min_notifications")
//    LiveData<List<MinNotificationEntity>> loadAllMinNotifications();
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertAll(List<MinNotificationEntity> minNotifications);
//
//    @Query("select * from min_notifications where id = :minNotificationID")
//    LiveData<MinNotificationEntity> loadMinNotification(int minNotificationID);
//
//    @Query("select * from min_notifications where id = :minNotificationID")
//    MinNotificationEntity loadMinNotificationASync(int minNotificationID);
}
