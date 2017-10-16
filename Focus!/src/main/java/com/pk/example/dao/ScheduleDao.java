package com.pk.example.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.pk.example.entity.ScheduleEntity;

import java.util.List;

@Dao
public interface ScheduleDao {
    @Query("SELECT * FROM schedules")
    LiveData<List<ScheduleEntity>> loadAllSchedules();

    @Insert
    void insert(ScheduleEntity schedule);

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertAll(List<ScheduleEntity> schedules);

    @Update
    void update(ScheduleEntity schedule);

    @Delete
    void delete(ScheduleEntity schedule);

    @Query("select * from schedules where _name = :scheduleName")
    LiveData<ScheduleEntity> loadSchedule(String scheduleName);

    @Query("select * from schedules where _name = :scheduleName")
    ScheduleEntity loadScheduleSync(String scheduleName);


}