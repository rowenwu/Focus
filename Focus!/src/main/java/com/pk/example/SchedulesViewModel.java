package com.pk.example;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.pk.example.dao.ScheduleDao;
import com.pk.example.entity.ScheduleEntity;

import java.util.List;

public class SchedulesViewModel extends AndroidViewModel {

    private final LiveData<List<ScheduleEntity>> schedules;

    private AppDatabase appDatabase;

    public SchedulesViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabase(this.getApplication());

        schedules = appDatabase.scheduleDao().loadAllSchedules();
    }


    public LiveData<List<ScheduleEntity>> getSchedules() {
        return schedules;
    }

    public void deleteItem(ScheduleEntity scheduleEntity) {
        //execute not working???
        new deleteAsyncTask(appDatabase).execute(scheduleEntity);
    }

    private static class deleteAsyncTask extends AsyncTask<ScheduleEntity, Void, Void> {

        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final ScheduleEntity... params) {
            db.scheduleDao().delete(params[0]);
            return null;
        }

    }

}