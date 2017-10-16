//package com.pk.example;
//
//import android.app.Application;
//import android.arch.lifecycle.AndroidViewModel;
//import android.arch.lifecycle.LiveData;
//
//import com.pk.example.entity.ScheduleEntity;
//
//import java.util.List;
//
//public class SchedulesViewModel extends AndroidViewModel {
//
//    private final LiveData<List<ScheduleEntity>> schedules;
//
//    private AppDatabase appDatabase;
//
//    public SchedulesViewModel(Application application) {
//        super(application);
//
//        appDatabase = AppDatabase.getDatabase(this.getApplication());
//
//        schedules = appDatabase.scheduleDao().loadAllSchedules();
//    }
//
//
//    public LiveData<List<BorrowModel>> getItemAndPersonList() {
//        return itemAndPersonList;
//    }
//
//    public void deleteItem(BorrowModel borrowModel) {
//        new deleteAsyncTask(appDatabase).execute(borrowModel);
//    }
//
//    private static class deleteAsyncTask extends AsyncTask<BorrowModel, Void, Void> {
//
//        private AppDatabase db;
//
//        deleteAsyncTask(AppDatabase appDatabase) {
//            db = appDatabase;
//        }
//
//        @Override
//        protected Void doInBackground(final BorrowModel... params) {
//            db.itemAndPersonModel().deleteBorrow(params[0]);
//            return null;
//        }
//
//    }
//
//}