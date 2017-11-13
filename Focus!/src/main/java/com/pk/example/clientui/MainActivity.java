package com.pk.example.clientui;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pk.example.R;
import com.pk.example.servicereceiver.NLService;

import java.util.Calendar;

public class MainActivity extends Activity {

    public TextView txtView;
//    private NotificationReceiver nReceiver;
    public Button profilesButton, schedulesButton, notificationsButton, weeklyButton;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);
        txtView = (TextView) findViewById(R.id.textView);
//        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NLService.INSERT_NOTIFICATION);
//        registerReceiver(nReceiver,filter);
        profilesButton = (Button) findViewById( R.id.btnNavigation );
        schedulesButton = (Button) findViewById( R.id.btnAllProfiles );
        notificationsButton = (Button) findViewById( R.id.btnNotificationList );
        weeklyButton = (Button) findViewById(R.id.btnWeeklyView);

        if(!hasUsageStatsPermission())
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));

        ComponentName cn = new ComponentName(getApplicationContext(), NLService.class);
        String flat = Settings.Secure.getString(getApplicationContext().getContentResolver(), "enabled_notification_listeners");
        final boolean enabled = flat != null && flat.contains(cn.flattenToString());
        if(!enabled){
            toggleService();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(nReceiver);
    }

    public void toggleService() {
        if (Build.VERSION.SDK_INT >= 18)
            gotoNotifyservice(this);
        else
            gotoAccessibility(this);
    }
    public static void gotoNotifyservice(Context context) {
        try {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            context.startActivity(intent);
        } catch (ActivityNotFoundException anfe) {
            try {
                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                context.startActivity(intent);
                Toast.makeText(context, context.getText(R.string.notification_listener_not_found_detour), Toast.LENGTH_LONG).show();
            } catch (ActivityNotFoundException anfe2) {
                Toast.makeText(context, anfe2.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public static void gotoAccessibility(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            context.startActivity(intent);
            Toast.makeText(context, context.getText(R.string.accessibility_toast), Toast.LENGTH_LONG).show();
        } catch (ActivityNotFoundException anfe) {
            try {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
                Toast.makeText(context, context.getText(R.string.accessibility_not_found_detour), Toast.LENGTH_LONG).show();
            } catch (ActivityNotFoundException anfe2) {
                Toast.makeText(context, anfe2.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


    public void buttonClicked(View v){

        if(v.getId() == R.id.btnCreateNotify){
            NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
            ncomp.setContentTitle("PK Notification");
            ncomp.setContentText("Notification Listener Example");
            ncomp.setTicker("Notification Listener Example");
            ncomp.setSmallIcon(R.drawable.ic_launcher);
            ncomp.setAutoCancel(true);
            nManager.notify((int)System.currentTimeMillis(),ncomp.build());
        }
        else if(v.getId() == R.id.btnNavigation){
            Intent i = new Intent(this, ScheduleListActivity.class);
            startActivity(i);
        }
//        else if(v.getId() == R.id.btnPermission){
//            toggleService(v);
//        }
        else if(v.getId() == R.id.btnAllProfiles){
            Intent i = new Intent(this, ProfileListActivity.class);
            i.putExtra("flag","create");
            startActivity(i);
        }
        else if(v.getId() == R.id.btnNotificationList){
            Intent i = new Intent(this, NotificationListActivity.class);
            startActivity(i);
        }
        else if (v.getId() == R.id.btnWeeklyView){
            Intent i = new Intent(this, CalendarActivity.class);
            startActivity(i);
        }

    }

    public boolean hasUsageStatsPermission(){
        boolean granted = false;
        AppOpsManager appOps = (AppOpsManager) context
                .getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), context.getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (context.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }
        return granted;
    }

//    class NotificationReceiver extends BroadcastReceiver{
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//
//            Log.i("intent ","intent "+intent.getExtras().toString());
//
//
//            String temp = intent.getExtras().getString("info")+ "\n----------------------------------------------" + txtView.getText();
//            txtView.setText(temp+"");
//        }
//    }
}
