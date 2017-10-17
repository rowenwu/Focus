package com.pk.example;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Build;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.pk.example.dao.CurrentNotificationListDao;
import com.pk.example.dao.ProfileDao;
import com.pk.example.entity.CurrentNotificationListEntity;

import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class NLService extends NotificationListenerService {
    static final String ADD_PROFILE = "com.pk.example.ADDPROFILE";
    static final String REMOVE_PROFILE = "com.pk.example.REMOVEPROFILE";
    static final String START_PROFILE_NOTIFICATION = " is now active and blocking your notifications.";
    static final String STOP_PROFILE_NOTIFICATION = " is now inactive. Check your missed notifications.";
    static final String ADD_SCHEDULE_PENDING_INTENT = "com.pk.example.ADDSCHEDULEPENDINGINTENT";
    static final String ADD_PROFILE_PENDING_INTENT = "com.pk.example.ADDPROFILEPENDINGINTENT";
    static final String CANCEL_ALARM_INTENTS = "com.pk.example.CANCELALARMINTENTS";


    private String TAG = this.getClass().getSimpleName();
    private SchedulingReceiver aReceiver;
    private HashMap<String, HashSet<String>> blockedApps;
    //store these separately so that when a schedule/profile is disabled/turned off, we can cancel the right pending intents
    private HashMap<String, HashSet<PendingIntent>> scheduleAlarmIntents;
    private HashMap<String, HashSet<PendingIntent>> profileAlarmIntents;
    private ProfileDao profileDao;
    private CurrentNotificationListDao crnDao;



    @Override
    public void onCreate() {
        super.onCreate();
        aReceiver = new SchedulingReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ADD_PROFILE);
        filter.addAction(REMOVE_PROFILE);
        filter.addAction(ADD_SCHEDULE_PENDING_INTENT);
        registerReceiver(aReceiver,filter);
        blockedApps = new HashMap<String, HashSet<String>>();
        scheduleAlarmIntents = new HashMap<String, HashSet<PendingIntent>>();
        profileAlarmIntents = new HashMap<String, HashSet<PendingIntent>>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(aReceiver);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        String statusBarNotificationKey = null;
        if (Build.VERSION.SDK_INT >= 20){
            statusBarNotificationKey = sbn.getKey();
            if(blockedApps.get(sbn.getPackageName()) != null)
                cancelNotification(statusBarNotificationKey);
        }


        handleActionAdd(sbn.getNotification(),
                sbn.getPackageName(),
                sbn.getTag(),
                sbn.getId(),
                statusBarNotificationKey,
                getApplicationContext(),
                "listener");
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG,"********** onNOtificationRemoved");

    }

    public void handleActionAdd(Notification notification, String packageName, String tag, int id, String key, Context context, String src) {


        String title = null;
        String text = null;


        // Get the text
        if (Build.VERSION.SDK_INT >= 21) {
            // Uncomment to test which extras a given notification contains
            /*for (String extraKey : notification.extras.keySet()) {
                Mlog.d(logTag, extraKey + "=" + notification.extras.get(extraKey));
            }*/
            try {
                title = notification.extras.get("android.title").toString();
            } catch (Exception ignored) {}
            try {
                text = notification.extras.get("android.text").toString();
            } catch (Exception ignored) {
                text = "";
            }

            String bigText = null;
            try {
                if (
                        notification.extras.getString("android.template", "").equals("android.app.Notification$InboxStyle")
                                && notification.extras.containsKey("android.textLines")
                        ) {
                    CharSequence[] textLines = notification.extras.getCharSequenceArray("android.textLines");
                    bigText = "";
                    if (textLines != null)
                        for (int i = textLines.length - 1; i >= 0; i--) {
                            CharSequence line = textLines[i];
                            bigText += line + "\n";
                        }
                } else {
                    bigText = notification.extras.get("android.bigText").toString();
                }
                if (notification.extras.containsKey("android.title.big"))
                    title = notification.extras.getCharSequence("android.title.big", title).toString();
            } catch (Exception ignored) {}

            if (bigText != null && bigText.length() > 3) {
                text = bigText.trim();
            }
        } else {
            // Old, hacky way. Close your eyes and skip this section.
            List<String> texts = null;
            try {
                texts = getText(notification);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (texts == null) {
                return;
            }
            if (texts.size() > 1) {
                text = texts.get(1);
            }
            if (text == null)
                text = String.valueOf(notification.tickerText);
            if (texts.size() == 0)
                texts.add(text);
            if (text == null || text.equals("null"))
                return;

            title = texts.get(0);


            // Get the full content in older Android versions. Really ugly.
            if (Build.VERSION.SDK_INT >= 16) {
                if (notification.bigContentView != null) {
                    try {
                        final String fullContent = fullContent(notification, context, texts, text);
                        if (fullContent != null) text = fullContent;
                    } catch (Resources.NotFoundException ignored) {
                    } catch (RuntimeException rte) {
                        try {
                            Looper.prepareMainLooper();
                        } catch (RuntimeException e) {
                            try {
                                final String fullContent = fullContent(notification, context, texts, text);
                                if (fullContent != null) text = fullContent;
                                // Ignore all errors, we'll survive without the full notification
                            } catch (Exception ignored) {}
                        }
                    } catch (Exception ignored) {}
                }
            }
        }

        final Intent intent = new  Intent("com.pk.example.NOTIFICATION_LISTENER_EXAMPLE");
        // Make an intent

        intent.putExtra("packageName", packageName);
        intent.putExtra("title", title);
        intent.putExtra("text", text);
        intent.putExtra("action", notification.contentIntent);

        if (Build.VERSION.SDK_INT >= 11)
            intent.putExtra("iconLarge", notification.largeIcon);
        intent.putExtra("icon", notification.icon);

        if (Build.VERSION.SDK_INT >= 21)
            intent.putExtra("color", notification.color);
        else if (Build.VERSION.SDK_INT >= 19)
            intent.putExtra("color", notification.extras.getInt("android.color"));

        intent.putExtra("tag", tag);
        intent.putExtra("id", id);
        intent.putExtra("key", key);


        if (Build.VERSION.SDK_INT >= 19) {
            try {
                Notification.Action[] actions = notification.actions;
                if (actions != null) {
                    intent.putExtra("actionCount", actions.length);

                    int i = actions.length;
                    for (Notification.Action action : actions) {
                        if (i < 0) break; //No infinite loops, has happened once
                        intent.putExtra("action" + i + "icon", action.icon);
                        intent.putExtra("action" + i + "title", action.title);
                        intent.putExtra("action" + i + "intent", action.actionIntent);
                        i--;
                    }
                }
            } catch (IllegalAccessError iae) {
                iae.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Build.VERSION.SDK_INT >= 16) {
            final SimpleAction[] actions = getActions(notification);

            int i = actions != null ? actions.length : 0;

            if (i > 0) {
                intent.putExtra("actionCount", actions.length);
                for (SimpleAction action : actions) {
                    if (i < 0) break; //No infinite loops, has happened once
                    intent.putExtra("action" + i + "icon", action.icon);
                    intent.putExtra("action" + i + "title", action.title);
                    intent.putExtra("action" + i + "intent", action.actionIntent);
                    i--;
                }
            }
        }


        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK +
                        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS +
                        Intent.FLAG_ACTIVITY_NO_ANIMATION
        );

        Log.i("intent ","intent "+intent.getExtras().toString());
        intent.putExtra("info",intent.getExtras().toString());
        sendBroadcast(intent);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private String fullContent(Notification notification, Context context, List<String> texts, String text) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup localView = (ViewGroup) inflater.inflate(notification.bigContentView.getLayoutId(), null);
        notification.bigContentView.reapply(context.getApplicationContext(), localView);

        ArrayList<View> allChildren = getAllChildren(localView);
        String viewTexts = "";
        for (View view : allChildren) {
            if (view instanceof TextView) {
                String mText = String.valueOf(((TextView) view).getText());
                if (!mText.equals(texts.get(0))
                        && mText.length() > 1
                        && !mText.matches("(([0]?[1-9]|1[0-2])([:.][0-5]\\d)(\\ [AaPp][Mm]))|(([0|1]?\\d?|2[0-3])([:.][0-5]\\d))")
                        && !view.getClass().getSimpleName().equals("Button")
                        ) {
                    //TODO: Check for texts identical to actions, as some apps doesn't use buttons for actions.
                    if (mText.startsWith(texts.get(0))) {
                        mText = mText.substring(texts.get(0).length());
                        if (mText.startsWith(":"))
                            mText = mText.substring(1);
                        if (mText.startsWith("\n"))
                            mText = mText.substring("\n".length());
                        if (mText.startsWith("\n"))
                            mText = mText.substring("\n".length());
                    }
                    viewTexts = viewTexts.concat(mText).concat("\n");
                }
            }
        }

        viewTexts = viewTexts.trim();

        if (viewTexts.length() > 1) {
            return viewTexts;
        }
        return null;
    }

    public SimpleAction[] getActions(Notification notification) {
        Object[] actionsAsObjects;
        try {
            Field field = Notification.class.getDeclaredField("actions");
            field.setAccessible(true);
            actionsAsObjects = (Object[]) field.get(notification);
            if (actionsAsObjects == null) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        int n = actionsAsObjects.length;
        SimpleAction[] actions = new SimpleAction[n];
        for (int i = 0; i < n; ++i) {
            Object object = actionsAsObjects[i];
            try {
                Field iconField = object.getClass().getDeclaredField("icon");
                Field titleField = object.getClass().getDeclaredField("title");
                Field actionIntentField = object.getClass().getDeclaredField("actionIntent");

                iconField.setAccessible(true);
                titleField.setAccessible(true);
                actionIntentField.setAccessible(true);

                int icon = iconField.getInt(object);
                CharSequence title = (CharSequence) titleField.get(object);
                PendingIntent actionIntent = (PendingIntent) actionIntentField.get(object);

                actions[i] = new SimpleAction(icon, title, actionIntent);
                continue;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        return actions;
    }

    public class SimpleAction {
        int icon;
        CharSequence title;
        PendingIntent actionIntent;

        public SimpleAction(int icon, CharSequence title, PendingIntent actionIntent) {
            this.icon = icon;
            this.title = title;
            this.actionIntent = actionIntent;
        }
    }



    private static List<String> getText(Notification notification) {
        RemoteViews contentView = notification.contentView;
        /*if (Build.VERSION.SDK_INT >= 16) {
            contentView = notification.bigContentView;
        }*/

        // Use reflection to examine the m_actions member of the given RemoteViews object.
        // It's not pretty, but it works.
        List<String> text = new ArrayList<>();
        try
        {
            Field field = contentView.getClass().getDeclaredField("mActions");
            field.setAccessible(true);

            @SuppressWarnings("unchecked")
            ArrayList<Parcelable> actions = (ArrayList<Parcelable>) field.get(contentView);

            // Find the setText() and setTime() reflection actions
            for (Parcelable p : actions)
            {
                Parcel parcel = Parcel.obtain();
                p.writeToParcel(parcel, 0);
                parcel.setDataPosition(0);

                // The tag tells which type of action it is (2 is ReflectionAction, from the source)
                int tag = parcel.readInt();
                if (tag != 2) continue;

                // View ID
                parcel.readInt();

                String methodName = parcel.readString();
                //noinspection ConstantConditions
                if (methodName == null) continue;

                    // Save strings
                else if (methodName.equals("setText"))
                {
                    // Parameter type (10 = Character Sequence)
                    parcel.readInt();

                    // Store the actual string
                    String t = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel).toString().trim();
                    if (!text.contains(t)) {
                        text.add(t);
                    }
                }

                // Save times. Comment this section out if the notification time isn't important
                /*else if (methodName.equals("setTime"))
                {
                    // Parameter type (5 = Long)
                    parcel.readInt();

                    String t = new SimpleDateFormat("h:mm a").format(new Date(parcel.readLong()));
                    text.add(t);
                }*/

                parcel.recycle();
            }
        }

        // It's not usually good style to do this, but then again, neither is the use of reflection...
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return text;
    }

    private ArrayList<View> getAllChildren(View v) {
        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<>();

        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }


    public void sendNotification(String message){
        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
        ncomp.setContentTitle("Focus!");
        ncomp.setContentText(message);
        ncomp.setTicker(message);
        ncomp.setSmallIcon(R.drawable.ic_launcher);
        ncomp.setAutoCancel(true);
        nManager.notify((int)System.currentTimeMillis(),ncomp.build());
    }

    public void addProfile(String profile){
        // get profile information (start time, end time, list of apps) from database
        // for list of apps, addBlockedApp
        //TODO IF PROFILE IS ALREADY ON ACTIVE PROFILES LIST THEN RETURN

        //TODO ADD PROFILE TO ACTIVE PROFILES LIST

        sendNotification(profile + START_PROFILE_NOTIFICATION);
//        Profile prof = DummyDb.getProfile(profile);
//        Profile prof = profileDao.loadProfileSync(profile);
//        for(int a = 0; a < prof.getAppsToBlock().size(); a++){
//            addBlockedApp(prof.appsToBlock.get(a), profile);
//        }

    }

    public void addBlockedApp(String appPackage, String profile) {
        HashSet<String> profiles = blockedApps.get(appPackage);
        if (profiles == null){
            profiles = new HashSet<String>();
        }
        profiles.add(profile);
        blockedApps.put(appPackage, profiles);

    }

    public void removeProfile(String profile){
        //TODO CHECK PROFILE IS ON ACTIVE PROFILES LIST, IF NOT RETURN
        //TODO REMOVE PROFILE FROM ACTIVE PROFILES LIST


        //TODO GET NOTIFICATIONS FROM THIS PROFILE AND MAKE IT THE PREVIOUS NOTIFICATIONS LIST
//        CurrentNotificationListEntity currs = crnDao.loadCurrvNotificationSync()

        sendNotification(profile + STOP_PROFILE_NOTIFICATION);
        Profile prof = DummyDb.getProfile(profile);
        for(int a = 0; a < prof.getAppsToBlock().size(); a++){
            addBlockedApp(prof.appsToBlock.get(a), profile);
        }

        HashSet<PendingIntent> alarms = profileAlarmIntents.get(profile);
        if (alarms != null) {
            for (PendingIntent temp : alarms) {
                ProfileScheduler.removeAlarm(getApplicationContext(), temp);
            }
            profileAlarmIntents.remove(profile);
        }

    }

    public void removeBlockedApp(String appPackage, String profile){
        HashSet<String> profiles = blockedApps.get(appPackage);
        if (profiles != null){
            profiles.remove(profile);
        }
        if(profiles.size() == 0)
            blockedApps.remove(appPackage);
    }

    public void addScheduleAlarmIntent(String schedule, PendingIntent piStart, PendingIntent piEnd){
        HashSet<PendingIntent> alarms = scheduleAlarmIntents.get(schedule);
        if (alarms == null){
            alarms = new HashSet<PendingIntent>();
        }
        alarms.add(piStart);
        alarms.add(piEnd);
        scheduleAlarmIntents.put(schedule, alarms);
    }

    public void addProfileAlarmIntent(String profile, PendingIntent pi){
        HashSet<PendingIntent> alarms = profileAlarmIntents.get(profile);
        if (alarms != null) {
            alarms = new HashSet<PendingIntent>();
        }
        alarms.add(pi);
        profileAlarmIntents.put(profile, alarms);
    }

    public void cancelScheduleAlarmIntents(String schedule){
        HashSet<PendingIntent> alarms = scheduleAlarmIntents.get(schedule);
        if (alarms != null){
            for (PendingIntent temp : alarms) {
                ProfileScheduler.removeAlarm(getApplicationContext(), temp);
            }
            scheduleAlarmIntents.remove(schedule);
        }
    }
    public void cancelProfileAlarmIntents(String profile){

    }

    // receives notice to start or stop profile from alarm
    class SchedulingReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("intent ","intent "+intent.getExtras().toString());

            String name = intent.getStringExtra("name");
            PendingIntent piStart, piEnd;

            switch(intent.getAction()){
                case ADD_PROFILE:
                    addProfile(name);
                    break;
                case REMOVE_PROFILE:
                    removeProfile(name);
                    break;
                case ADD_SCHEDULE_PENDING_INTENT:
                    //  get parcelable and add to profileAlarmIntent
                    piStart = intent.getParcelableExtra("startIntent");
                    piEnd = intent.getParcelableExtra("endIntent");
                    addScheduleAlarmIntent(name, piStart, piEnd);
                    break;
                case ADD_PROFILE_PENDING_INTENT:
                    //  get parcelable and add to profileAlarmIntent
                    piEnd = intent.getParcelableExtra("pendingIntent");
                    addProfileAlarmIntent(name, piEnd);
                    break;
                case CANCEL_ALARM_INTENTS:
                    cancelScheduleAlarmIntents(name);
                    break;
            }

        }
    }
}
