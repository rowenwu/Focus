package com.pk.example;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.pk.example.entity.MinNotificationEntity;
import com.pk.example.entity.PreviousNotificationListEntity;

import java.util.ArrayList;
import java.util.List;



public class NotificationListViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;

    private final LiveData<List<MinNotificationEntity>> notificationList;

    public NotificationListViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabase(this.getApplication());

        notificationList = appDatabase.previousNotificationListDao().loadAllPrevNotifications();
    }


    public LiveData<List<MinNotificationEntity>> getNotificationList() {
        return notificationList;
    }
}
