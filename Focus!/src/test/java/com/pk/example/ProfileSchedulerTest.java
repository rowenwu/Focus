package com.pk.example;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class ProfileSchedulerTest {
    @Mock
    Context context;

    private boolean isAlarmSet() {
        Intent intent = new Intent(NLService.ADD_PROFILE);
        long id = Calendar.getInstance().getTimeInMillis();
        String [] strings = new String [] {"com.facebook.orca", "com.appname.fake", "com.something.else"};
        ArrayList<String> appsToBlock = new ArrayList<String>(Arrays.asList(strings));
        ProfileScheduler.createAlarm(context,  "profile", appsToBlock, new Date(), 0, 0, true, NLService.ADD_PROFILE, id);
        PendingIntent service = PendingIntent.getService(
                context,
                (int) id,
                intent,
                PendingIntent.FLAG_NO_CREATE
        );
        return service != null;
    }

    @Test
    public void testSetAlarm() throws Exception {
        assertTrue(isAlarmSet());
    }
}