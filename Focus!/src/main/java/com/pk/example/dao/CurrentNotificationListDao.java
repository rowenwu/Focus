//package com.pk.example.dao;
//
//import android.arch.lifecycle.LiveData;
//import android.arch.persistence.room.Dao;
//import android.arch.persistence.room.Insert;
//import android.arch.persistence.room.OnConflictStrategy;
//import android.arch.persistence.room.Query;
//
//import com.pk.example.entity.ScheduleEntity;
//
//import java.util.List;
//
//
//@Dao
//public interface CurrentNotificationListDao {
//    @Query("SELECT * FROM prev_notifications")
//    LiveData<List<CurrentNotificationListEntity>> loadAllPrevNotifications();
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertAll(List<CurrentNotificationListEntity> prevNotifications);
//
//    @Query("select * from prev_notifications where id = :prevNotificationID")
//    LiveData<CurrentNotificationListEntity> loadPrevNotification(int prevNotificationID);
//
//    @Query("select * from prev_notifications where id = :prevNotificationID")
//    ScheduleEntity loadPrevNotificationSync(int prevNotificationID);
//}
