package com.pk.example.servicereceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.pk.example.database.AppDatabase;
import com.pk.example.entity.ProfileEntity;

import static com.pk.example.servicereceiver.NLService.ADD_PROFILE;
import static com.pk.example.servicereceiver.NLService.REMOVE_PROFILE;

/**
 * Created by kathe on 11/3/2017.
 */

public class ProfileScheduleReceiver  extends BroadcastReceiver {
    private AppDatabase db;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        db = AppDatabase.getDatabase(context);
        context = context;
        String name;

        //TODO CHANGE NAME TO ID

        switch(intent.getAction()) {
            case ADD_PROFILE:
                name = intent.getStringExtra("name");
                new UpdateProfile(name, true).execute();
                break;
            case REMOVE_PROFILE:
                name = intent.getStringExtra("name");
                new UpdateProfile(name, false).execute();
                //                new ChangePrevNotifications(intent.getStringArrayListExtra("profiles")).execute();
                break;
        }
    }


    private class UpdateProfile extends AsyncTask<Void, Void, Void> {
        private String name;
        private boolean active;

        public UpdateProfile(String name, boolean active){
            super();
            this.name = name;
            this.active = active;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ProfileEntity profile = db.profileDao().loadProfileSync(name);
            if(profile != null){
                profile.setActive(active);
                db.profileDao().update(profile);
            }

            return null;
        }
    }
}
