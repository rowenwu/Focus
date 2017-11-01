package com.pk.example.dao;

//import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.pk.example.Schedule;
import com.pk.example.entity.ProfileEntity;
import com.pk.example.entity.ScheduleEntity;

import java.util.List;

@Dao
public interface ScheduleDao {

//    //get all schedule
//    @Query("SELECT * FROM schedules")
//    LiveData<List<ScheduleEntity>> loadAllSchedules();

    //get all schedule
    @Query("SELECT * FROM schedules")
    List<ScheduleEntity> loadAllSchedulesSync();


    @Query("DELETE FROM schedules")
    void deleteAll();


    /*
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ScheduleEntity> schedules);
    */

    //insert a new schedule
    @Insert
    void insert(ScheduleEntity schedule);

    //delete a schedule
    @Delete
    void delete(ScheduleEntity schedule);

    //update a profile
    @Update
    void update(ScheduleEntity schedule);
//
//    //get a schedule
//    @Query("select * from schedules where _name = :scheduleName")
//    LiveData<ScheduleEntity> loadSchedule(String scheduleName);

    @Query("select * from schedules where _name = :scheduleName")
    ScheduleEntity loadScheduleSync(String scheduleName);

}