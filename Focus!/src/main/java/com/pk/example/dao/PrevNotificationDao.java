package com.pk.example.dao;


//import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.pk.example.MinNotification;
import com.pk.example.entity.MinNotificationEntity;
import com.pk.example.entity.PrevNotificationEntity;
import com.pk.example.entity.ProfileEntity;

import java.util.List;

@Dao
public interface PrevNotificationDao {
//    @Query("SELECT * FROM prev_notifications")
//    LiveData<List<PrevNotificationEntity>> loadAllPrevNotifications();

    @Query("SELECT * FROM prev_notifications")
    List<PrevNotificationEntity> loadAllPrevNotificationsSync();

    @Delete
    void delete(PrevNotificationEntity notification);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(PrevNotificationEntity notification);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PrevNotificationEntity> notifications);

    @Query("DELETE FROM prev_notifications")
    void deleteAll();

//    @Query("select * from prev_notifications where _id = :notificationID")
//    LiveData<MinNotificationEntity> loadPrevNotification(int notificationID);
//
//    @Query("select * from min_notifications where _id = :notificationID")
//    MinNotificationEntity loadPrevNotificationASync(int notificationID);

}