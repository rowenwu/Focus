package com.pk.example.servicereceiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import com.pk.example.database.AppDatabase;
import com.pk.example.entity.ProfileEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.pk.example.servicereceiver.NLService.ADD_PROFILE;
import static com.pk.example.servicereceiver.NLService.REMOVE_PROFILE;

/**
 * Created by kathe on 11/3/2017.
 */

public class ProfileScheduleReceiver  extends BroadcastReceiver {
    private AppDatabase db;
    private Context mContext;


    @Override
    public void onReceive(Context context, Intent intent) {
        db = AppDatabase.getDatabase(context);
        this.mContext = context;
        String name = intent.getStringExtra("name");
        ArrayList<String> apps = intent.getStringArrayListExtra("apps");
        Intent i= new Intent(context, ForegroundDetectorService.class);
        i.putExtra("name", name);
        i.putExtra("apps", apps);
        //TODO CHANGE NAME TO ID

        switch(intent.getAction()) {
            case ADD_PROFILE:
                new UpdateProfile(name, true).execute();
                i.setAction(ADD_PROFILE);
                break;
            case REMOVE_PROFILE:
                new UpdateProfile(name, false).execute();
                i.setAction(REMOVE_PROFILE);
                break;
        }

        context.startService(i);
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
