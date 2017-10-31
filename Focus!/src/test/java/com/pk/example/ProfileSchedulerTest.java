//package com.pk.example;
//
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//
//import static org.junit.Assert.*;
//import static org.robolectric.Shadows.shadowOf;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.robolectric.RobolectricTestRunner;
//import org.robolectric.RuntimeEnvironment;
//import org.robolectric.annotation.Config;
//import org.robolectric.shadows.ShadowAlarmManager;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Date;
//
//@RunWith(RobolectricTestRunner.class)
//@Config(constants = BuildConfig.class)
//public class ProfileSchedulerTest {
//
//    Context context;
//    ShadowAlarmManager shadowAlarmManager;
//
//
//    @Before
//    public void setUp() throws Exception {
//        context = RuntimeEnvironment.application.getApplicationContext();
//        AlarmManager alarmManager = (AlarmManager) RuntimeEnvironment.application.getSystemService(Context.ALARM_SERVICE);
//        shadowAlarmManager = shadowOf(alarmManager);
//        Intent intent = new Intent(NLService.ADD_PROFILE);
//        long id = Calendar.getInstance().getTimeInMillis();
//        String [] strings = new String [] {"com.facebook.orca", "com.appname.fake", "com.something.else"};
//        ArrayList<String> appsToBlock = new ArrayList<String>(Arrays.asList(strings));
//        PendingIntent pi = ProfileScheduler.createPendingIntent(context, "profile", appsToBlock, NLService.ADD_PROFILE, id);
//        ProfileScheduler.setAlarm(context, new Date() , 1, 0,  true, pi);
//
//    }
//
//
//    @Test
//    public void testSetAlarm() throws Exception {
//        ShadowAlarmManager.ScheduledAlarm scheduledAlarm = shadowAlarmManager.getNextScheduledAlarm();
//        assertEquals(AlarmController.INTERVAL_ONE_HOUR, scheduledAlarm.interval);
//    }
//}