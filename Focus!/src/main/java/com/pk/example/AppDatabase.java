package com.pk.example;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.pk.example.dao.CurrentNotificationListDao;
import com.pk.example.dao.MinNotificationDao;
import com.pk.example.dao.PreviousNotificationListDao;
import com.pk.example.dao.ProfileDao;
import com.pk.example.dao.ScheduleDao;
import com.pk.example.entity.MinNotificationEntity;
import com.pk.example.entity.ProfileEntity;
import com.pk.example.entity.ScheduleEntity;
import com.pk.example.entity.PreviousNotificationListEntity;
import com.pk.example.entity.CurrentNotificationListEntity;

@Database(entities = {MinNotificationEntity.class, ProfileEntity.class, ScheduleEntity.class, PreviousNotificationListEntity.class, CurrentNotificationListEntity.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "db")
                            .build();
        }
        return INSTANCE;
    }
    public abstract MinNotificationDao minNotificationDao();

    public abstract ProfileDao profileDao();

    public abstract ScheduleDao scheduleDao();

    public abstract CurrentNotificationListDao currentNotificationListDao();

    public abstract PreviousNotificationListDao previousNotificationListDao();
}