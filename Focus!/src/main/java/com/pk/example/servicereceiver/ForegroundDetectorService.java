package com.pk.example.servicereceiver;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.pk.example.R;
import com.pk.example.clientui.ProfileListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;
import static com.pk.example.servicereceiver.NLService.ADD_PROFILE;
import static com.pk.example.servicereceiver.NLService.REMOVE_PROFILE;

/**
 * Created by kathe on 11/12/2017.
 */

public class ForegroundDetectorService extends Service {
//    HashMap<String, Thread> threads;
    Thread t;
    private HashMap<String, ArrayList<String>> blockedApps;
    public static final String BLOCKED_APP_OPENED_NOTIFICATION = "Focus! is blocking notifications from ";
    public static final int ONGOING_NOTIFICATION_ID = 101;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
//        threads = new HashMap<String, Thread>();
        if(blockedApps == null)
            blockedApps = new HashMap<String, ArrayList<String>>();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String name = intent.getStringExtra("name");
        switch (intent.getAction()) {
            case ADD_PROFILE:
                addProfile(name, intent.getStringArrayListExtra("apps"));
                if(t==null){
                    t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            startJob();
                        }
                    });
                    t.start();
                }
                break;
            case REMOVE_PROFILE:
                removeProfile(name, intent.getStringArrayListExtra("apps"));
                if(blockedApps.size() == 0){
                    if(t != null)
                        t.interrupt();
                    stopSelf();
                }
                break;
        }

        return START_STICKY;
    }

    public void makeForeground(String app){
        Intent notificationIntent = new Intent(this, ProfileListActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Notification notification = builder.setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setColor(getResources().getColor(R.color.default_color))
                .setContentTitle("Focus!")
                .setContentIntent(pendingIntent)
                .setContentText(BLOCKED_APP_OPENED_NOTIFICATION + app)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(bigText)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setOngoing(true)
                .build();
//        Notification notification =
//                new Notification.Builder(this)
//                        .setContentTitle("Focus!")
//                        .setContentText(BLOCKED_APP_OPENED_NOTIFICATION + app)
//                        .setSmallIcon(R.drawable.ic_launcher)
//                        .setContentIntent(pendingIntent)
//                        .setTicker(BLOCKED_APP_OPENED_NOTIFICATION + app)
//                        .setPriority(Notification.PRIORITY_HIGH)
//                        .build();

        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

    public void removeForeground(){
        stopForeground(true);
    }

    private void startJob(){
        //do job here
        String foregroundApp = getForegroundApp();
        if(blockedApps.get(foregroundApp) != null){
//            sendNotification("hi");
            makeForeground(foregroundApp);
        }
        else{
//            sendNotification("bye");
            removeForeground();
        }
        //job completed. Rest for 5 second before doing another one
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //do job again
        startJob();
    }

    public void sendNotification(String message) {
        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
        ncomp.setContentTitle("Focus!");
        ncomp.setContentText(message);
        ncomp.setTicker(message);
        ncomp.setSmallIcon(R.drawable.ic_launcher);
        ncomp.setAutoCancel(true);
        nManager.notify((int) System.currentTimeMillis(), ncomp.build());
    }


    private String getForegroundApp() {
        String currentApp = "NULL";
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - 1000*1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }
        return currentApp;
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
