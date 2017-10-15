package com.pk.example;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.pk.example.dao.MinNotificationDao;
import com.pk.example.dao.ProfileDao;
import com.pk.example.dao.ScheduleDao;
import com.pk.example.entity.MinNotificationEntity;
import com.pk.example.entity.ProfileEntity;
import com.pk.example.entity.ScheduleEntity;

@Database(entities = {MinNotificationEntity.class, ProfileEntity.class, ScheduleEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    static final String DATABASE_NAME = "db";

    public abstract MinNotificationDao minNotificationDao();

    public abstract ProfileDao profileDao();

    public abstract ScheduleDao scheduleDao();
}