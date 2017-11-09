package com.pk.example.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.pk.example.database.dao.MinNotificationDao;
import com.pk.example.database.dao.PrevNotificationDao;
import com.pk.example.database.dao.ProfileDao;
import com.pk.example.database.dao.ScheduleDao;
import com.pk.example.entity.MinNotificationEntity;
import com.pk.example.entity.PrevNotificationEntity;
import com.pk.example.entity.ProfileEntity;
import com.pk.example.entity.ScheduleEntity;

@Database(entities = {MinNotificationEntity.class, ProfileEntity.class, ScheduleEntity.class,
        PrevNotificationEntity.class}, version = 17)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
//            context.deleteDatabase("db");
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "db")
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }
    public abstract MinNotificationDao minNotificationDao();

    public abstract ProfileDao profileDao();

    public abstract ScheduleDao scheduleDao();

    public abstract PrevNotificationDao prevNotificationDao();

//    public abstract CurrentNotificationListDao currentNotificationListDao();

//    public abstract PreviousNotificationListDao previousNotificationListDao();
}