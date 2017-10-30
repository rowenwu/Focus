//package com.pk.example;
//
//
//import android.app.PendingIntent;
//import android.content.Intent;
//
//import static org.junit.Assert.*;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//public class ProfileSchedulerTest {
//
//    private boolean isAlarmSet() {
//        Intent intent = new Intent(NLService.ADD_PROFILE);
//        PendingIntent service = PendingIntent.getService(
//                context,
//                0,
//                intent,
//                PendingIntent.FLAG_NO_CREATE
//        );
//        return service != null;
//    }
//
//}