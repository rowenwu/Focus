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
    private Handler handler;
    private String appOpen;
    private HashMap<String, ArrayList<String>> blockedApps;

    @Override
    public void onReceive(Context context, Intent intent) {
        db = AppDatabase.getDatabase(context);
        if(blockedApps == null)
            blockedApps = new HashMap<String, ArrayList<String>>();
        this.mContext = context;
        String name = intent.getStringExtra("name");


        //TODO CHANGE NAME TO ID

        switch(intent.getAction()) {
            case ADD_PROFILE:
                new UpdateProfile(name, true).execute();
                addProfile(name, intent.getStringArrayListExtra("apps"));
                if(handler == null) {
                    handler = new Handler();
                    appOpen = "com.pk.example";
                    final int delay = 1000; //milliseconds

                    handler.postDelayed(new Runnable() {
                        public void run() {
                            //do something
                            try {
                                boolean blockedAppOpened = new ForegroundCheckTask().execute(mContext).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            handler.postDelayed(this, delay);
                        }
                    }, delay);
                }
                break;
            case REMOVE_PROFILE:
                removeProfile(name, intent.getStringArrayListExtra("apps"));

                if(blockedApps.size() == 0)
                    handler.removeCallbacksAndMessages(null);

                name = intent.getStringExtra("name");
                new UpdateProfile(name, false).execute();
                //                new ChangePrevNotifications(intent.getStringArrayListExtra("profiles")).execute();

                //TODO STOP HANDLER ONCE NO PROFILES ARE ACTIVE
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

    class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        && blockedApps.get(appProcess.processName) != null){
//                        && !appProcess.processName.equals(appOpen)) {
                    appOpen = appProcess.processName;
                    Intent i = new Intent(NLService.BLOCKED_APP_OPENED);
                    i.putExtra("packageName", appOpen);
                    i.putExtra("name", blockedApps.get(appProcess.processName).get(0));

                    mContext.sendBroadcast(i);
                    return true;
                }
            }
            return false;
        }
    }

    public void addBlockedApp(String appPackage, String profile) {
        ArrayList<String> profiles = blockedApps.get(appPackage);
        if (profiles == null) {
            profiles = new ArrayList<String>();
        }
        if(!profiles.contains(profile))
            profiles.add(profile);
        blockedApps.put(appPackage, profiles);

    }


    public void removeBlockedApp(String appPackage, String profile) {
        ArrayList<String> profiles = blockedApps.get(appPackage);
        if (profiles != null) {
            profiles.remove(profile);
            if (profiles.size() == 0)
                blockedApps.remove(appPackage);
        }
    }

    public void addProfile(String profile, ArrayList<String> appsToBlock) {
        for (int a = 0; a < appsToBlock.size(); a++) {
            addBlockedApp(appsToBlock.get(a), profile);
        }

    }

    public void removeProfile(String profile, ArrayList<String> appsToBlock) {
        for (int a = 0; a < appsToBlock.size(); a++) {
            removeBlockedApp(appsToBlock.get(a), profile);
        }
    }


}
