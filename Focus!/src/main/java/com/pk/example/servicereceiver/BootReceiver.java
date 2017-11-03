package com.pk.example.servicereceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.pk.example.entity.ProfileEntity;

/**
 * Created by kathe on 11/3/2017.
 */

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("Action: " + intent.getAction() + "\n");
        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
        String log = sb.toString();
//        Log.d(TAG, log);
        Toast.makeText(context, log, Toast.LENGTH_LONG).show();


    }
//
//    private class CheckForActiveSchedules extends AsyncTask<Void, Void, Void> {
//        private String profileName;
//        private boolean active;
//
//        public CheckForActiveSchedules(String profileName, boolean active){
//            super();
//            this.profileName = profileName;
//            this.active = active;
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            //check if any schedules that are enabled should be active but have not been set to active
//
//            ProfileEntity profile = db.profileDao().loadProfileSync(profileName);
//            if(profile != null){
//                profile.setActive(active);
//                db.profileDao().update(profile);
//            }
//
//            return null;
//        }
//    }
}